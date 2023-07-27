package com.example.posts.controllers;

import com.example.posts.dto.PostRequest;
import com.example.posts.dto.PostResponse;
import com.example.posts.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping
    public ResponseEntity<Page<PostResponse>> findAll(Pageable pageable) {
        Page<PostResponse> result = service.findAll(pageable);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        PostResponse result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<PostResponse> insert(@RequestBody PostRequest dto) {
        PostResponse response = service.insert(dto);
        return ResponseEntity.ok().body(response);
    }
}
