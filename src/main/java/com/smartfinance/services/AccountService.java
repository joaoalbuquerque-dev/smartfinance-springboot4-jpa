package com.smartfinance.services;

import com.smartfinance.entities.Account;
import com.smartfinance.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account findById(Long id) {
       Optional<Account> obj = repository.findById(id);
       return obj.get();
    }
}
