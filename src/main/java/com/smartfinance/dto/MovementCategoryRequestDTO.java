package com.smartfinance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovementCategoryRequestDTO(
        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be greater than zero")
        Long categoryId,

        @NotNull(message = "Category amount is required")
        @Positive(message = "Category amount must be greater than zero")
        Double amount) {
}
