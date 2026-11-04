package com.smartfinance.dto;

import com.smartfinance.entities.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountRequestDTO(
        @NotBlank(message = "Bank is required")
        String bank,

        @NotNull(message = "Balance is required")
        Double balance,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be greater than zero")
        Long userId) {
}
