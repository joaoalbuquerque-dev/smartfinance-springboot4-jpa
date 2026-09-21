package com.smartfinance.services;

import com.smartfinance.entities.User;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
       Optional<User> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public User insert(User obj) {
        return repository.save(obj);
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

    public User update(Long id, User obj) {
        User entity = repository.getReferenceById(id);
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(User entity, User obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
    }
}
