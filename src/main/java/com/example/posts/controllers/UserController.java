package com.example.posts.controllers;

import com.example.posts.dto.UserRequest;
import com.example.posts.dto.UserResponse;
import com.example.posts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest dto) {
        UserResponse response = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id ,@Valid @RequestBody UserRequest dto) {
        UserResponse response = service.update(id, dto);
        return ResponseEntity.ok().body(response);
    }
}
