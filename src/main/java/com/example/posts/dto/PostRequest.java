package com.example.posts.dto;

import java.time.Instant;

public record PostRequest(String text, Instant postDate) {
}
