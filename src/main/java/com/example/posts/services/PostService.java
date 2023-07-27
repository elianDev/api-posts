package com.example.posts.services;

import com.example.posts.dto.PostDTO;
import com.example.posts.entities.Post;
import com.example.posts.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Transactional(readOnly = true)
    public Page<PostDTO> findAll(Pageable pageable) {
        Page<Post> result = repository.findAll(pageable);
        return result.map(x -> new PostDTO(x.getId(), x.getText(), x.getPostDate(), x.getUser().getId()));
    }

    @Transactional(readOnly = true)
    public PostDTO findById(Long id) {
        Post post = repository.findById(id).get();
        PostDTO dto = new PostDTO(post.getId(), post.getText(), post.getPostDate(), post.getUser().getId());
        return dto;
    }
}
