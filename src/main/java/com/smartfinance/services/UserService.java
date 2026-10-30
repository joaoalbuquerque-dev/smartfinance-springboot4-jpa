package com.smartfinance.services;

import com.smartfinance.dto.UserRequestDTO;
import com.smartfinance.dto.UserResponseDTO;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
       return new UserResponseDTO(findEntityById(id));
    }

    @Transactional
    public UserResponseDTO insert(UserRequestDTO dto) {
        User user = new User(null, dto.name(), dto.email(), dto.password());
        return new UserResponseDTO(repository.save(user));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
       findEntityById(id);
       try {
           repository.deleteById(id);
       } catch (DataIntegrityViolationException e) {
           throw new DataBaseException(e.getMessage());
       }
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User entity = findEntityById(id);
        updateData(entity, dto);
        return new UserResponseDTO(repository.save(entity));
    }

    private User findEntityById(Long id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    private void updateData(User entity, UserRequestDTO dto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            entity.setPassword(dto.password());
        }
    }
}
