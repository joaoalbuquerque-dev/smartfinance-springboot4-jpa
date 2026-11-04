package com.smartfinance.dto;

import com.smartfinance.entities.enums.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record MovementRequestDTO(
        String description,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotNull(message = "Transaction date is required")
        Instant transactionDate,

        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Account ID is required")
        @Positive(message = "Account ID must be greater than zero")
        Long accountId,

        @NotEmpty(message = "Movement must have at least one category")
        Set<@NotNull(message = "Movement category is required")
                @Valid MovementCategoryRequestDTO> categories,

        @NotNull(message = "Installments list is required")
        List<@NotNull(message = "Installment is required")
                @Valid InstallmentRequestDTO> installments) {
}
