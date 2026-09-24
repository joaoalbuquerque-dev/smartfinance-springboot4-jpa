package com.smartfinance.services;

import com.smartfinance.entities.Movement;
import com.smartfinance.repositories.MovementRepository;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService {

    @Autowired
    private MovementRepository repository;

    public List<Movement> findAll() {
        return repository.findAll();
    }

    public Movement findById(Long id) {
       Optional<Movement> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public Movement insert(Movement obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Movement update(Long id, Movement obj) {
        Movement entity = repository.getReferenceById(id);
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(Movement entity, Movement obj) {
        entity.setDescription(obj.getDescription());
        entity.setAccount(obj.getAccount());
        entity.setTransactionType(obj.getTransactionType());
        entity.setMovementCategories(obj.getMovementCategories());
        entity.setAmount(obj.getAmount());
    }
}
