package com.smartfinance.dto;

import com.smartfinance.entities.enums.AccountType;

public record AccountRequestDTO(String bank, Double balance,
                                AccountType accountType, Long userId) {
}
