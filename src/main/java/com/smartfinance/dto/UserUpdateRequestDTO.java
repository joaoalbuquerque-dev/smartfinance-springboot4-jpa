package com.smartfinance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @Size(min = 6, message = "Password must have at least 6 characters")
        String password) {

}
