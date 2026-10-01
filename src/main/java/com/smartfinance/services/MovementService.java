package com.smartfinance.services;

import com.smartfinance.entities.Account;
import com.smartfinance.entities.Movement;
import com.smartfinance.entities.MovementCategory;
import com.smartfinance.entities.enums.TransactionType;
import com.smartfinance.entities.pk.MovementCategoryPK;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.MovementCategoryRepository;
import com.smartfinance.repositories.MovementRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.InvalidAmountException;
import com.smartfinance.services.exceptions.InvalidMovementCategoryException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService {

    @Autowired
    private MovementRepository repository;
    @Autowired
    private MovementCategoryRepository movementCategoryRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<Movement> findAll() {
        return repository.findAll();
    }

    public Movement findById(Long id) {
       Optional<Movement> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public Movement insert(Movement obj) {
        if(obj.getAmount() <= 0.0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        validateCategories(obj);
        validateAmountsNegative(obj);
        validateAmountCategories(obj);

        Account account = accountRepository.findById(obj.getAccount().getId())
                .orElseThrow(()-> new ResourceNotFoundExcepetion(obj.getAccount().getId()));

        if(obj.getTransactionType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - obj.getAmount());
        }

        else if(obj.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance() + obj.getAmount());
        }
        repository.save(obj);
        accountRepository.save(account);

        for(MovementCategory mc : obj.getMovementCategories()) {
            mc.setMovement(obj);
            movementCategoryRepository.save(mc);
         }
        return obj;
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundExcepetion(id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Movement update(Long id, Movement obj) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundExcepetion(id);
        }
        Movement entity = repository.getReferenceById(id);
        updateData(entity, obj);

        for (MovementCategory mc : obj.getMovementCategories()) {
            MovementCategoryPK pk = new MovementCategoryPK();
            pk.setMovement(entity);
            pk.setCategory(mc.getCategory());

            MovementCategory entityMc =
                    movementCategoryRepository.findById(pk)
                            .orElseThrow(()-> new ResourceNotFoundExcepetion(id));
            entityMc.setAmount(mc.getAmount());
            movementCategoryRepository.save(entityMc);
        }
        return repository.save(entity);
    }

    private void updateData(Movement entity, Movement obj) {
        entity.setDescription(obj.getDescription());
        entity.setTransactionDate(obj.getTransactionDate());
        entity.setAccount(obj.getAccount());
        entity.setTransactionType(obj.getTransactionType());
        entity.setAmount(obj.getAmount());
    }

    private void validateCategories(Movement obj) {
        if (obj.getMovementCategories().isEmpty()) {
            throw new InvalidMovementCategoryException("Movement must have at least one category");
        }

        for (MovementCategory mc : obj.getMovementCategories()) {
            if (mc.getCategory() == null) {
                throw new InvalidMovementCategoryException("Category cannot be null");
            }
        }
    }

    private void validateAmountsNegative(Movement obj) {
        for (MovementCategory mc : obj.getMovementCategories()) {
            if (mc.getAmount() <= 0.0) {
                throw new InvalidAmountException("Category amount must be greater than zero");
            }
        }
    }

    private void validateAmountCategories(Movement obj) {
        double totalCategories = 0.0;

        for (MovementCategory mc : obj.getMovementCategories()) {
            totalCategories += mc.getAmount();
        }
        if(totalCategories != obj.getAmount()) {
            throw new InvalidAmountException("The amounts don't match.");
        }
    }

}
