package com.example.posts.services;

import com.example.posts.dto.PostRequest;
import com.example.posts.dto.PostResponse;
import com.example.posts.entities.Post;
import com.example.posts.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
        Post post = repository.findById(id).get();
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
        Post entity = repository.getReferenceById(id);
        entity.setText(dto.text());
        entity = repository.save(entity);
        return new PostResponse(entity.getId(), entity.getText(), entity.getPostDate(), 1L);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private PostResponse createDto(Post entity) {
        return new PostResponse(entity.getId(),
                entity.getText(),
                entity.getPostDate(),
                entity.getUser().getId());
    }
}
