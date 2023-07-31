package com.example.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotBlank(message = "Required field")
                          @Size(min = 3, max = 80, message = "Name must be between 3 to 80 characters")
                          String name,
                          @NotBlank(message = "Required field")
                          String email,
                          @NotBlank(message = "Required field")
                          @Size(min = 6, message = "Password must have at least 6 characters")
                          String password) {
}
