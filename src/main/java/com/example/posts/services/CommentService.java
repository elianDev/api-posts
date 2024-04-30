package com.example.posts.services;

import com.example.posts.dto.CommentRequest;
import com.example.posts.dto.CommentResponse;
import com.example.posts.dto.UserDTO;
import com.example.posts.entities.Comment;
import com.example.posts.entities.Post;
import com.example.posts.entities.User;
import com.example.posts.repositories.CommentRepository;
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
public class CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository repository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> findAll(Pageable pageable) {
        Page<Comment> result = repository.findAll(pageable);
        return result.map(x -> createDto(x));
    }

    @Transactional(readOnly = true)
    public CommentResponse findById(Long id) {
        Comment Comment = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found"));
        CommentResponse dto = createDto(Comment);
        return dto;
    }

    @Transactional
    public CommentResponse insert(CommentRequest dto) {
        UserDTO userDto = userService.getMe();
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Comment entity = new Comment();
        entity.setText(dto.text());
        entity.setCreatedAt(Instant.now());
        entity.setUser(user);
        entity.setPost(post);

        entity = repository.save(entity);
        return createDto(entity);
    }

    @Transactional
    public CommentResponse update(Long id, CommentRequest dto) {
        try {
            Comment entity = repository.getReferenceById(id);
            if (entity.getUser().getId() != userService.getMe().getId()) {
                throw new ResourceNotFoundException("User not allowed");
            } else {
                entity.setText(dto.text());
                Post post = postRepository.findById(dto.postId())
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

                entity.setPost(post);
                entity = repository.save(entity);
                return createDto(entity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    @Transactional
    public void delete(Long id) {
        try {
            Comment Comment = repository.findById(id).get();
            if(Comment.getUser().getId() != userService.getMe().getId()) {
                throw new ResourceNotFoundException("User not allowed");
            } else repository.delete(Comment);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    private CommentResponse createDto(Comment entity) {
        return new CommentResponse(entity.getId(),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getUser().getId(),
                entity.getPost().getId());
    }
}
