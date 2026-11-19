package com.smartfinance.dto;

import com.smartfinance.entities.Account;
import com.smartfinance.entities.enums.AccountType;

public record AccountResponseDTO(Long id, String bank, Double balance,
                                 AccountType accountType, String userName) {

    public AccountResponseDTO(Account account) {
        this(account.getId(), account.getBank(), account.getBalance(),
                account.getAccountType(), account.getUser().getName());
    }
}
