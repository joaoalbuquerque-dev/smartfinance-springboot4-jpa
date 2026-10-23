package com.smartfinance.dto;

import com.smartfinance.entities.Movement;
import com.smartfinance.entities.enums.TransactionType;

import java.time.Instant;
import java.util.List;

public record MovementResponseDTO(Long id, String description, Double amount,
                                  Instant transactionDate,
                                  TransactionType transactionType, Long accountId,
                                  List<MovementCategoryResponseDTO> categories,
                                  List<InstallmentResponseDTO> installments) {

    public MovementResponseDTO(Movement movement) {
        this(movement.getId(), movement.getDescription(), movement.getAmount(),
                movement.getTransactionDate(), movement.getTransactionType(),
                movement.getAccount().getId(),
                movement.getMovementCategories().stream()
                        .map(MovementCategoryResponseDTO::new)
                        .toList(),
                movement.getInstallments().stream()
                        .map(InstallmentResponseDTO::new)
                        .toList());
    }
}
