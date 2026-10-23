package com.smartfinance.dto;

import com.smartfinance.entities.MovementCategory;

public record MovementCategoryResponseDTO(Long categoryId, String categoryName,
                                          Double amount) {

    public MovementCategoryResponseDTO(MovementCategory movementCategory) {
        this(movementCategory.getCategory().getId(),
                movementCategory.getCategory().getName(),
                movementCategory.getAmount());
    }
}
