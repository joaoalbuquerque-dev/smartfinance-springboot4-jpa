package com.smartfinance.services;

import com.smartfinance.dto.CategoryRequestDTO;
import com.smartfinance.dto.CategoryResponseDTO;
import com.smartfinance.entities.Category;
import com.smartfinance.entities.User;
import com.smartfinance.repositories.CategoryRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        User user = userService.authenticated();

        return repository.findByUserIsNullOrUser_Id(user.getId()).stream()
                .map(CategoryResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {return new CategoryResponseDTO(findEntityById(id));}

    @Transactional
    public CategoryResponseDTO insert(CategoryRequestDTO dto) {
        User user = userService.authenticated();
        Category category = (new Category(null, dto.name(), user));
        category = repository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO insertGlobal(CategoryRequestDTO dto) {
        User user = userService.authenticated();

        if(!user.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can create global categories");
        }
        Category category = new Category(null, dto.name(), null);
        category = repository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        Category entity = findEntityById(id);
        validateWritePermission(entity);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category entity = findEntityById(id);
        validateWritePermission(entity);
        updateData(entity, dto);
        return new CategoryResponseDTO(repository.save(entity));
    }

    private Category findEntityById(Long id) {
        User user = userService.authenticated();
        Optional<Category> obj = repository.findAvailableById(id, user.getId());
        return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    private void updateData(Category entity, CategoryRequestDTO dto) {
        entity.setName(dto.name());
    }

    private void validateWritePermission(Category category) {
        User user = userService.authenticated();

        if (category.getUser() == null && !user.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can modify global categories");
        }
    }
}
