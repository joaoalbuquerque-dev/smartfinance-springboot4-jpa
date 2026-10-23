package com.smartfinance.dto;

import com.smartfinance.entities.enums.TransactionType;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record MovementRequestDTO(String description, Double amount,
                                 Instant transactionDate,
                                 TransactionType transactionType,
                                 Long accountId,
                                 Set<MovementCategoryRequestDTO> categories,
                                 List<InstallmentRequestDTO> installments) {
}
