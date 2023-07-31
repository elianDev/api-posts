package com.example.posts.services;

import com.example.posts.dto.UserRequest;
import com.example.posts.dto.UserResponse;
import com.example.posts.entities.User;
import com.example.posts.repositories.UserRepository;
import com.example.posts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

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
        entity.setPassword(dto.password());

        entity = repository.save(entity);
        return createDto(entity);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest dto) {
        try {
            User entity = repository.getReferenceById(id);
            entity.setName(dto.name());
            entity.setEmail(dto.email());
            entity.setPassword(dto.password());
            entity = repository.save(entity);
            return new UserResponse(entity.getId(), entity.getName(), entity.getEmail());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    @Transactional
    public void delete(Long id) {
        try {
            User user = repository.findById(id).get();
            repository.delete(user);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    private UserResponse createDto(User entity) {
        return new UserResponse(entity.getId(),entity.getName(), entity.getEmail());
    }
}
