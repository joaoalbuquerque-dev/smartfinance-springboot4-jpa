package com.smartfinance.services;

import com.smartfinance.entities.*;
import com.smartfinance.entities.enums.InstallmentStatus;
import com.smartfinance.entities.enums.TransactionType;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.CategoryRepository;
import com.smartfinance.repositories.MovementCategoryRepository;
import com.smartfinance.repositories.MovementRepository;
import com.smartfinance.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class MovementService {

    @Autowired
    private MovementRepository repository;
    @Autowired
    private MovementCategoryRepository movementCategoryRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Movement> findAll() {
        return repository.findAll();
    }

    public Movement findById(Long id) {
       Optional<Movement> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    @Transactional
    public Movement insert(Movement obj) {
        if(obj.getAmount() <= 0.0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        validateCategories(obj);
        validateAmountCategories(obj);

        Account account = accountRepository.findById(obj.getAccount().getId())
                .orElseThrow(()-> new ResourceNotFoundExcepetion(obj.getAccount().getId()));

        if (!obj.getInstallments().isEmpty()) {
            calculateInstallmentAmounts(obj);
            calculateInstallmentsDueData(obj);
        }
        validateInstallment(obj);

        if (obj.getInstallments().isEmpty()) {

            if (obj.getTransactionType() == TransactionType.EXPENSE) {
                account.setBalance(account.getBalance() - obj.getAmount());
            }
            else if (obj.getTransactionType() == TransactionType.INCOME) {
                account.setBalance(account.getBalance() + obj.getAmount());
            }
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

    @Transactional
    public Movement update(Long id, Movement obj) {
        validateBasicData(obj);

        Movement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepetion(id));

        validateAccountUnchanged(entity, obj);
        validateCategories(obj);
        validateAmountCategories(obj);

        if (entity.getInstallments().stream()
                .anyMatch(installment -> installment.getStatus() == InstallmentStatus.PAID)) {
            throw new InvalidInstallmentException(
                    "Cannot update a movement with paid installments"
            );
        }

        boolean wasInstallmentMovement = !entity.getInstallments().isEmpty();
        TransactionType oldType = entity.getTransactionType();
        double oldAmount = entity.getAmount();

        prepareAndValidateInstallments(obj);
        boolean isInstallmentMovement = !obj.getInstallments().isEmpty();

        if (!wasInstallmentMovement) {
            reverseBalance(entity.getAccount(), oldType, oldAmount);
        }

        updateData(entity, obj);
        synchronizeInstallments(entity, obj);
        synchronizeCategories(entity, obj);

        if (!isInstallmentMovement) {
            applyBalance(entity.getAccount(),
                    entity.getTransactionType(), entity.getAmount());
        }

        return repository.save(entity);
    }

    private void updateData(Movement entity, Movement obj) {
        entity.setDescription(obj.getDescription());
        entity.setTransactionDate(obj.getTransactionDate());
        entity.setTransactionType(obj.getTransactionType());
        entity.setAmount(obj.getAmount());
    }

    private void validateBasicData(Movement obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Movement cannot be null");
        }

        if (obj.getAmount() == null
                || !Double.isFinite(obj.getAmount())
                || obj.getAmount() <= 0.0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (obj.getTransactionType() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        if (obj.getAccount() == null || obj.getAccount().getId() == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        if (obj.getInstallments() == null) {
            throw new InvalidInstallmentException("Installments cannot be null");
        }
    }

    private void validateAccountUnchanged(Movement entity, Movement obj) {
        Long currentAccountId = entity.getAccount().getId();
        Long requestedAccountId = obj.getAccount().getId();

        if (!java.util.Objects.equals(currentAccountId, requestedAccountId)) {
            throw new IllegalArgumentException(
                    "Changing the account of an existing movement is not allowed"
            );
        }
    }

    private void prepareAndValidateInstallments(Movement obj) {
        if (obj.getInstallments().isEmpty()) {
            return;
        }

        for (int i = 0; i < obj.getInstallments().size(); i++) {
            Installment installment = obj.getInstallments().get(i);

            if (installment == null
                    || installment.getNumber() == null
                    || installment.getNumber() != i + 1) {
                throw new InvalidInstallmentException(
                        "Installment numbers must be sequential"
                );
            }
        }

        if (obj.getInstallments().getFirst().getDueDate() == null) {
            throw new InvalidInstallmentException(
                    "First installment due date cannot be null"
            );
        }

        calculateInstallmentAmounts(obj);
        calculateInstallmentsDueData(obj);
        validateInstallment(obj);
    }

    private void synchronizeInstallments(Movement entity, Movement obj) {
        entity.getInstallments().removeIf(current ->
                obj.getInstallments().stream()
                        .noneMatch(desired ->
                                desired.getNumber().equals(current.getNumber()))
        );

        for (Installment desired : obj.getInstallments()) {
            Installment current = entity.getInstallments().stream()
                    .filter(installment ->
                            installment.getNumber().equals(desired.getNumber()))
                    .findFirst()
                    .orElse(null);

            if (current != null) {
                current.setAmount(desired.getAmount());
                current.setDueDate(desired.getDueDate());
            } else {
                Installment newInstallment = new Installment();
                newInstallment.setNumber(desired.getNumber());
                newInstallment.setAmount(desired.getAmount());
                newInstallment.setDueDate(desired.getDueDate());
                newInstallment.setStatus(InstallmentStatus.PENDING);
                newInstallment.setMovement(entity);

                entity.getInstallments().add(newInstallment);
            }
        }
    }

    private void synchronizeCategories(Movement entity, Movement obj) {
        Map<Long, MovementCategory> currentCategories = new HashMap<>();

        for (MovementCategory current : entity.getMovementCategories()) {
            currentCategories.put(current.getCategory().getId(), current);
        }

        for (MovementCategory requested : obj.getMovementCategories()) {
            Long categoryId = requested.getCategory().getId();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundExcepetion(categoryId));

            MovementCategory current = currentCategories.remove(categoryId);

            if (current != null) {
                current.setAmount(requested.getAmount());
            } else {
                MovementCategory newMovementCategory =
                        new MovementCategory(entity, category, requested.getAmount());

                entity.getMovementCategories().add(newMovementCategory);
                movementCategoryRepository.save(newMovementCategory);
            }
        }

        List<MovementCategory> removedCategories =
                new ArrayList<>(currentCategories.values());

        if (!removedCategories.isEmpty()) {
            entity.getMovementCategories().removeAll(removedCategories);
            movementCategoryRepository.deleteAll(removedCategories);
        }
    }

    private void reverseBalance(Account account,
                                TransactionType transactionType,
                                double amount) {
        if (transactionType == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() + amount);
        } else if (transactionType == TransactionType.INCOME) {
            account.setBalance(account.getBalance() - amount);
        }
    }

    private void applyBalance(Account account,
                              TransactionType transactionType,
                              double amount) {
        if (transactionType == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - amount);
        } else if (transactionType == TransactionType.INCOME) {
            account.setBalance(account.getBalance() + amount);
        }
    }

    private void validateCategories(Movement obj) {
        Set<Long> categoryIds = new HashSet<>();
        if (obj.getMovementCategories() == null || obj.getMovementCategories().isEmpty()) {
            throw new InvalidMovementCategoryException("Movement must have at least one category");
        }
        for (MovementCategory mc : obj.getMovementCategories()) {

            if (mc == null) {
                throw new InvalidMovementCategoryException("Movement category cannot be null");
            }
            if (mc.getCategory() == null) {
                throw new InvalidMovementCategoryException("Category cannot be null");
            }
            if (mc.getCategory().getId() == null) {
                throw new InvalidMovementCategoryException("Category ID cannot be null");
            }

          categoryRepository.findById(mc.getCategory().getId())
                    .orElseThrow(()-> new ResourceNotFoundExcepetion(mc.getCategory().getId()));

            if (!categoryIds.add(mc.getCategory().getId())) {
                throw new InvalidMovementCategoryException("Category cannot be duplicated");
            }
        }
    }

    private void validateAmountCategories(Movement obj) {
        double totalCategories = 0.0;

        for (MovementCategory mc : obj.getMovementCategories()) {

            if (mc.getAmount() <= 0.0) {
                throw new InvalidAmountException("Category amount must be greater than zero");
            }
            totalCategories += mc.getAmount();
        }
        if(totalCategories != obj.getAmount()) {
            throw new InvalidAmountException("The amounts don't match.");
        }
     }

    private void validateInstallment(Movement obj) {

        if (obj.getInstallments().isEmpty()) {
            return;
        }
        double totalAmountInstallments = 0.0;
        Set<Integer> numbers = new HashSet<>();

        for (Installment installment : obj.getInstallments()) {
            //numero > 0
            if (installment.getNumber() != null && installment.getNumber() <= 0) {
                throw new InvalidInstallmentException("Installment number must be greater than zero");
            }

            if (installment.getAmount() <= 0.0) {
                throw new InvalidAmountException("Installment amount must be greater than zero");
            }
            //numero duplicado
            if (!numbers.add(installment.getNumber())) {
                throw new InvalidInstallmentException("Duplicate installment number");
            }
            //soma das parcelas
            totalAmountInstallments += installment.getAmount();

            //dueDate obrigatório
            if (installment.getDueDate() == null) {
                throw new InvalidInstallmentException("Installment due date cannot be null");
            }
        }
        //valida soma das parcelas
        if (totalAmountInstallments != obj.getAmount()) {
            throw new InvalidAmountException("The installment amounts don't match the movement amount");
        }
        for (int i = 0; i < obj.getInstallments().size(); i++) {
            Installment installment = obj.getInstallments().get(i);

            //valida sequência das parcelas
            if (installment.getNumber() == null || installment.getNumber() != i + 1) {
                throw new InvalidInstallmentException("Installment numbers must be sequential");
            }
        }

        for (int i = 1; i < obj.getInstallments().size(); i++) {

            Installment current = obj.getInstallments().get(i);
            Installment previous = obj.getInstallments().get(i - 1);

            if (!current.getDueDate().isAfter(previous.getDueDate())) {
                throw new InvalidInstallmentException("Installment due dates must be in ascending order");
            }
        }
    }

    private void calculateInstallmentAmounts(Movement obj) {

        double installmentAmount =
                Math.round(obj.getAmount() / obj.getInstallments().size() * 100.0) / 100.0;

        for (int i = 0; i < obj.getInstallments().size(); i++) {

            Installment installment = obj.getInstallments().get(i);

            if (i == obj.getInstallments().size() - 1) {

                double previousAmount = installmentAmount * i;
                double lastAmount = Math.round((obj.getAmount() - previousAmount) * 100.0) / 100.00;

                installment.setAmount(lastAmount);

            } else {

                installment.setAmount(installmentAmount);
            }
        }
    }

    private void calculateInstallmentsDueData(Movement obj) {
        LocalDate firstDate = obj.getInstallments().getFirst().getDueDate();

        for (int i = 0; i < obj.getInstallments().size(); i++) {
            Installment installment = obj.getInstallments().get(i);

            installment.setDueDate(firstDate.plusMonths(i));

        }
    }

}
