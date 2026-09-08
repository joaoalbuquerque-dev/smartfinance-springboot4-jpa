package com.smartfinance.services;

import com.smartfinance.entities.Movement;
import com.smartfinance.repositories.MovementRepository;
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
       return obj.get();
    }
}
