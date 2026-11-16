package com.smartfinance.dto;

import com.smartfinance.entities.User;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record UserResponseDTO(Long id, String name, String email, List<String> roles) {

    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }


}
