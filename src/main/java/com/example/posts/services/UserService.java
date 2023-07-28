package com.example.posts.services;

import com.example.posts.dto.UserRequest;
import com.example.posts.dto.UserResponse;
import com.example.posts.entities.User;
import com.example.posts.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = repository.findById(id).get();
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
        User entity = repository.getReferenceById(id);
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(dto.password());
        entity = repository.save(entity);
        return new UserResponse(entity.getId(), entity.getName(), entity.getEmail());
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private UserResponse createDto(User entity) {
        return new UserResponse(entity.getId(),entity.getName(), entity.getEmail());
    }
}
