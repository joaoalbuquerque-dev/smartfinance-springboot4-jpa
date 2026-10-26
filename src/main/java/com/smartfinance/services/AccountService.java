package com.smartfinance.services;

import com.smartfinance.dto.AccountRequestDTO;
import com.smartfinance.dto.AccountResponseDTO;
import com.smartfinance.entities.Account;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(AccountResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO findById(Long id) {
       return new AccountResponseDTO(findEntityById(id));
    }

    @Transactional
    public AccountResponseDTO insert(AccountRequestDTO dto) {
        User user = findUserById(dto.userId());
        Account account = new Account(null, dto.bank(), dto.balance(),
                dto.accountType(), user);
        return new AccountResponseDTO(repository.save(account));
    }

    @Transactional
    public void delete(Long id) {
        findEntityById(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Transactional
    public AccountResponseDTO update(Long id, AccountRequestDTO dto) {
        Account entity = findEntityById(id);
        User user = findUserById(dto.userId());
        updateData(entity, dto, user);
        return new AccountResponseDTO(repository.save(entity));
    }

    private Account findEntityById(Long id) {
        Optional<Account> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
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
