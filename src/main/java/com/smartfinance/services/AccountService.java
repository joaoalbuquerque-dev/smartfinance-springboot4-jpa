package com.smartfinance.services;

import com.smartfinance.dto.AccountRequestDTO;
import com.smartfinance.dto.AccountResponseDTO;
import com.smartfinance.dto.UserResponseDTO;
import com.smartfinance.entities.Account;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> findAll() {
        User user = userService.authenticated();
        return repository.findByUserId(user.getId()).stream()
                .map(AccountResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO findById(Long id) {
       User user = userService.authenticated();
        Account account = repository.findByIdAndUserId(id, user.getId()).orElseThrow(()-> new ResourceNotFoundExcepetion(id));
        return new AccountResponseDTO(account);
    }

    @Transactional
    public AccountResponseDTO insert(AccountRequestDTO dto) {
        User user = userService.authenticated();
        Account account = new Account(null, dto.bank(), dto.balance(),
                dto.accountType(), user);
        return new AccountResponseDTO(repository.save(account));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        User user = userService.authenticated();
        Account account = repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundExcepetion(id));
        try {
            repository.delete(account);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Transactional
    public AccountResponseDTO update(Long id, AccountRequestDTO dto)  {
        User user = userService.authenticated();
        Account entity = findEntityById(id);
        if(!entity.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to modify this account");
        }
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
    }
}
