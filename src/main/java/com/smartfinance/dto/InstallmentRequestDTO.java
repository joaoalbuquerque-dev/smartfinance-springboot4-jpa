package com.smartfinance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record InstallmentRequestDTO(
        @NotNull(message = "Installment number is required")
        @Positive(message = "Installment number must be greater than zero")
        Integer number,

        @NotNull(message = "Installment due date is required")
        LocalDate dueDate) {
}
