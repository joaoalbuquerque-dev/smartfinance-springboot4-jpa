package com.smartfinance.dto;

import com.smartfinance.entities.User;

public record UserResponseDTO(Long id, String name, String email) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
