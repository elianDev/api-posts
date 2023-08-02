package com.example.posts.dto;

import com.example.posts.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private List<String> roles = new ArrayList<>();

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        for (GrantedAuthority role: entity.getRoles()) {
            roles.add(role.getAuthority());
        }
    }
}
