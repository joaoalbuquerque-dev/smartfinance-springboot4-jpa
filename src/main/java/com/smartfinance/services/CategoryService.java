package com.smartfinance.services;

import com.smartfinance.dto.CategoryRequestDTO;
import com.smartfinance.entities.Category;
import com.smartfinance.repositories.CategoryRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(Long id) {
       Optional<Category> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public Category insert(CategoryRequestDTO dto) {
        return repository.save(new Category(null, dto.name()));
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

    public Category update(Long id, CategoryRequestDTO dto) {
        Category entity = findById(id);
        updateData(entity, dto);
        return repository.save(entity);
    }

    private void updateData(Category entity, CategoryRequestDTO dto) {
        entity.setName(dto.name());
    }
}
