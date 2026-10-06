package com.smartfinance.services;

import com.smartfinance.entities.Payment;
import com.smartfinance.repositories.PaymentRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository repository;

    public List<Payment> findAll() {
        return repository.findAll();
    }

    public Payment findById(Long id) {
        Optional<Payment> obj = repository.findById(id);
        return obj.orElseThrow(()-> new ResourceNotFoundExcepetion(id));
    }

    public Payment insert(Payment obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundExcepetion(id);
        } try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }


}
