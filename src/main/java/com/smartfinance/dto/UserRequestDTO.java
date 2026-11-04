package com.smartfinance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required", groups = OnCreate.class)
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password) {

    public interface OnCreate {
    }
}
