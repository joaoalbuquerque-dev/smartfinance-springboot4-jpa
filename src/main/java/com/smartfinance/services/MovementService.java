package com.smartfinance.services;

import com.smartfinance.entities.Movement;
import com.smartfinance.entities.MovementCategory;
import com.smartfinance.entities.pk.MovementCategoryPK;
import com.smartfinance.repositories.MovementCategoryRepository;
import com.smartfinance.repositories.MovementRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService {

    @Autowired
    private MovementRepository repository;

    @Autowired
    private MovementCategoryRepository movementCategoryRepository;

    public List<Movement> findAll() {
        return repository.findAll();
    }

    public Movement findById(Long id) {
       Optional<Movement> obj = repository.findById(id);
       return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    public Movement insert(Movement obj) {
        repository.save(obj);

        for(MovementCategory mc : obj.getMovementCategories()) {
            mc.setMovement(obj);
            movementCategoryRepository.save(mc);
        }
        return obj;
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundExcepetion(id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Movement update(Long id, Movement obj) {
        Movement entity = repository.getReferenceById(id);
        updateData(entity, obj);

        for (MovementCategory mc : obj.getMovementCategories()) {
            MovementCategoryPK pk = new MovementCategoryPK();
            pk.setMovement(entity);
            pk.setCategory(mc.getCategory());

            MovementCategory entityMc =
                    movementCategoryRepository.findById(pk)
                            .orElseThrow(()-> new ResourceNotFoundExcepetion(id));
            entityMc.setAmount(mc.getAmount());
            movementCategoryRepository.save(entityMc);
        }
        return repository.save(entity);
    }

    private void updateData(Movement entity, Movement obj) {
        entity.setDescription(obj.getDescription());
        entity.setAccount(obj.getAccount());
        entity.setTransactionType(obj.getTransactionType());
        entity.setAmount(obj.getAmount());
    }
}
