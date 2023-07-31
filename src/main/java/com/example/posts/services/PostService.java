package com.example.posts.services;

import com.example.posts.dto.PostRequest;
import com.example.posts.dto.PostResponse;
import com.example.posts.entities.Post;
import com.example.posts.entities.User;
import com.example.posts.repositories.PostRepository;
import com.example.posts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Transactional(readOnly = true)
    public Page<PostResponse> findAll(Pageable pageable) {
        Page<Post> result = repository.findAll(pageable);
        return result.map(x -> createDto(x));
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        Post post = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        PostResponse dto = createDto(post);
        return dto;
    }

    @Transactional
    public PostResponse insert(PostRequest dto) {
        Post entity = new Post();
        entity.setText(dto.text());
        entity.setPostDate(Instant.now());

        entity = repository.save(entity);
        return new PostResponse(entity.getId(), entity.getText(), entity.getPostDate(), 1L);
    }

    @Transactional
    public PostResponse update(Long id, PostRequest dto) {
        try {
            Post entity = repository.getReferenceById(id);
            entity.setText(dto.text());
            entity = repository.save(entity);
            return new PostResponse(entity.getId(), entity.getText(), entity.getPostDate(), 1L);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    @Transactional
    public void delete(Long id) {
        try {
            Post post = repository.findById(id).get();
            repository.delete(post);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    private PostResponse createDto(Post entity) {
        return new PostResponse(entity.getId(),
                entity.getText(),
                entity.getPostDate(),
                entity.getUser().getId());
    }
}
