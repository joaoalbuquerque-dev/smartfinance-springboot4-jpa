package com.smartfinance.services;

import com.smartfinance.entities.Account;
import com.smartfinance.entities.Installment;
import com.smartfinance.entities.Movement;
import com.smartfinance.entities.Payment;
import com.smartfinance.entities.enums.InstallmentStatus;
import com.smartfinance.entities.enums.TransactionType;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.InstallmentRepository;
import com.smartfinance.repositories.PaymentRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.InvalidPaymentException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository repository;
    @Autowired
    InstallmentRepository installmentRepository;
    @Autowired
    AccountRepository accountRepository;

    public List<Payment> findAll() {
        return repository.findAll();
    }

    public Payment findById(Long id) {
        Optional<Payment> obj = repository.findById(id);
        return obj.orElseThrow(()-> new ResourceNotFoundExcepetion(id));
    }

    @Transactional
    public Payment insert(Payment obj) {
        Installment installment = installmentRepository.findById(obj.getInstallment().getId())
                .orElseThrow(()-> new ResourceNotFoundExcepetion(obj.getInstallment().getId()));

                if (installment.getStatus() == InstallmentStatus.PAID) {
                    throw new InvalidPaymentException("Installment is already paid");
                }
                obj.setInstallment(installment);

                Account account = installment.getMovement().getAccount();
                if (installment.getMovement().getTransactionType() == TransactionType.EXPENSE) {
                        account.setBalance(account.getBalance() - installment.getAmount());
                } else if (installment.getMovement().getTransactionType() == TransactionType.INCOME) {
                    account.setBalance(account.getBalance() + installment.getAmount());
                }
                accountRepository.save(account);
                installment.setStatus(InstallmentStatus.PAID);
                obj.setPaidAt(Instant.now());
                installmentRepository.save(installment);

        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        Payment payment = repository.findById(id).orElseThrow(()-> new ResourceNotFoundExcepetion(id));
        Installment installment = payment.getInstallment();
        installment.setPayment(null);
        reversePaymentBalance(installment);
        installment.setStatus(InstallmentStatus.PENDING);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private void reversePaymentBalance(Installment installment) {
        Movement movement = installment.getMovement();
        Account account = movement.getAccount();
        if (installment.getStatus() == InstallmentStatus.PAID) {
            if (movement.getTransactionType() == TransactionType.EXPENSE) {
                account.setBalance(account.getBalance() + installment.getAmount());
            } else if (movement.getTransactionType() == TransactionType.INCOME) {
                account.setBalance(account.getBalance() - installment.getAmount());
            }
        }
        accountRepository.save(account);
    }


}
