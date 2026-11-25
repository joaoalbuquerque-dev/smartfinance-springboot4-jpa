package com.smartfinance.dto;

import com.smartfinance.entities.Category;

public record CategoryResponseDTO(Long id, String name, boolean global) {

    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getUser() == null);
    }
}
