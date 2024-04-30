package com.example.posts.services;

import com.example.posts.dto.UserDTO;
import com.example.posts.dto.UserRequest;
import com.example.posts.dto.UserResponse;
import com.example.posts.entities.Role;
import com.example.posts.entities.User;
import com.example.posts.projections.UserDetailsProjection;
import com.example.posts.repositories.RoleRepository;
import com.example.posts.repositories.UserRepository;
import com.example.posts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found")
        );
        UserResponse dto = createDto(user);
        return dto;
    }

    @Transactional
    public UserResponse insert(UserRequest dto) {
        User entity = new User();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        entity.setPassword(encryptedPassword);

        Role role = roleRepository.findByAuthority("ROLE_USER"); // Assumindo que "ROLE_USER" é o papel padrão para novos usuários
        entity.addRole(role);

        entity = repository.save(entity);
        return createDto(entity);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest dto) {
        try {
            User entity = repository.getReferenceById(id);
            if(entity.getId() == authenticated().getId()) {
                entity.setName(dto.name());
                entity.setEmail(dto.email());
                entity.setPassword(dto.password());
                entity = repository.save(entity);
                return new UserResponse(entity.getId(), entity.getName(), entity.getEmail());
            }
            else throw new ResourceNotFoundException("User not allowed");
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    @Transactional
    public void delete(Long id) {
        try {
            User user = repository.findById(id).get();
            if(authenticated().getId() == user.getId()) repository.delete(user);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    private UserResponse createDto(User entity) {
        return new UserResponse(entity.getId(),entity.getName(), entity.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if(result.size() == 0) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection: result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return repository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = authenticated();
        return new UserDTO(user);
    }
}
