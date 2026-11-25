package com.smartfinance.repositories;

import com.smartfinance.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIsNullOrUser_Id(Long userId);

    @Query("""
    SELECT c FROM Category c
    WHERE c.id = :id
    AND (c.user IS NULL OR c.user.id = :userId)
    """)
    Optional<Category> findAvailableById(
            @Param("id") Long id,
            @Param("userId") Long userId);
}
