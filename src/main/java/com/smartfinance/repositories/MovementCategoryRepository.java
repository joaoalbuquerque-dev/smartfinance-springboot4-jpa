package com.smartfinance.repositories;

import com.smartfinance.entities.MovementCategory;
import com.smartfinance.entities.pk.MovementCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementCategoryRepository extends JpaRepository<MovementCategory, MovementCategoryPK> {
}
