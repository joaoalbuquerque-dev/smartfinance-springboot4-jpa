package com.smartfinance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDTO(
        @NotNull(message = "Installment ID is required")
        @Positive(message = "Installment ID must be greater than zero")
        Long installmentId) {
}
