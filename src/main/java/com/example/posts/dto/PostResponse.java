package com.example.posts.dto;

import java.time.Instant;

public record PostResponse(Long id, String text, Instant postDate, Long userId) {
}
