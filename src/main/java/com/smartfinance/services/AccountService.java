package com.smartfinance.services;

import com.smartfinance.dto.AccountRequestDTO;
import com.smartfinance.entities.Account;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account findById(Long id) {
       Optional<Account> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public Account insert(AccountRequestDTO dto) {
        User user = findUserById(dto.userId());
        Account account = new Account(null, dto.bank(), dto.balance(),
                dto.accountType(), user);
        return repository.save(account);
    }

    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundExcepetion(id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Account update(Long id, AccountRequestDTO dto) {
        Account entity = findById(id);
        User user = findUserById(dto.userId());
        updateData(entity, dto, user);
        return repository.save(entity);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExcepetion(userId));
    }

    private void updateData(Account entity, AccountRequestDTO dto, User user) {
        entity.setBank(dto.bank());
        entity.setBalance(dto.balance());
        entity.setAccountType(dto.accountType());
        entity.setUser(user);
    }
}
