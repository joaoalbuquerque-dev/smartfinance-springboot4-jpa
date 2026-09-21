package com.smartfinance.services;

import com.smartfinance.entities.Category;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.CategoryRepository;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
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
       return obj.get();
    }

    public Category insert(Category obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Category update(Long id, Category obj) {
        Category entity = repository.getReferenceById(id);
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(Category entity, Category obj) {
        entity.setName(obj.getName());
    }
}
