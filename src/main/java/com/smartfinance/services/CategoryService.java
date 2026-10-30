package com.smartfinance.services;

import com.smartfinance.dto.CategoryRequestDTO;
import com.smartfinance.dto.CategoryResponseDTO;
import com.smartfinance.entities.Category;
import com.smartfinance.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(CategoryResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
       return new CategoryResponseDTO(findEntityById(id));
    }

    @Transactional
    public CategoryResponseDTO insert(CategoryRequestDTO dto) {
        Category category = repository.save(new Category(null, dto.name()));
        return new CategoryResponseDTO(category);
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
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category entity = findEntityById(id);
        updateData(entity, dto);
        return new CategoryResponseDTO(repository.save(entity));
    }

    private Category findEntityById(Long id) {
        Optional<Category> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    private void updateData(Category entity, CategoryRequestDTO dto) {
        entity.setName(dto.name());
    }
}
