package com.example.posts.controllers;

import com.example.posts.dto.PostDTO;
import com.example.posts.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping
    public ResponseEntity<Page<PostDTO>> findAll(Pageable pageable) {
        Page<PostDTO> result = service.findAll(pageable);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable Long id) {
        PostDTO result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }
}
