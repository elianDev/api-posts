package com.example.posts.controllers;

import com.example.posts.dto.CommentRequest;
import com.example.posts.dto.CommentResponse;
import com.example.posts.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<Page<CommentResponse>> findAll(Pageable pageable) {
        Page<CommentResponse> result = service.findAll(pageable);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        CommentResponse result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<CommentResponse> insert(@Valid @RequestBody CommentRequest dto) {
        CommentResponse response = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id ,@Valid @RequestBody CommentRequest dto) {
        CommentResponse response = service.update(id, dto);
        return ResponseEntity.ok().body(response);
    }
}
