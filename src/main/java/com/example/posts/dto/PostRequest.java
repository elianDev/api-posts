package com.example.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record PostRequest(@NotBlank(message = "Required field")
                          @Size(max = 500, message = "500 character limit")
                          String text,
                          Instant postDate) {
}
