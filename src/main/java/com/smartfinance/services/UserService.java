package com.smartfinance.services;

import com.smartfinance.dto.UserRequestDTO;
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

    public User insert(UserRequestDTO dto) {
        User user = new User(null, dto.name(), dto.email(), dto.password());
        return repository.save(user);
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

    public User update(Long id, UserRequestDTO dto) {
        User entity = findById(id);
        updateData(entity, dto);
        return repository.save(entity);
    }

    private void updateData(User entity, UserRequestDTO dto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            entity.setPassword(dto.password());
        }
    }
}
