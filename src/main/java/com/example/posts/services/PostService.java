package com.example.posts.services;

import com.example.posts.dto.PostDTO;
import com.example.posts.entities.Post;
import com.example.posts.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Transactional(readOnly = true)
    public List<PostDTO> findAll() {
        List<PostDTO> result = repository.findAll().
                stream().
                map(post -> new PostDTO(
                        post.getId(),
                        post.getText(),
                        post.getPostDate(),
                        post.getUser().getId())).
                toList();
        return result;
    }

    @Transactional(readOnly = true)
    public PostDTO findById(Long id) {
        Post post = repository.findById(id).get();
        PostDTO dto = new PostDTO(post.getId(), post.getText(), post.getPostDate(), post.getUser().getId());
        return dto;
    }
}
