package com.example.posts.dto;

import java.time.Instant;

public record PostDTO(Long id, String text, Instant postDate, Long userId) {
}
