package com.example.posts.dto;

import java.time.Instant;

public record CommentResponse(Long id, String text, Instant createdAt, Long userId, Long postId) {
}
