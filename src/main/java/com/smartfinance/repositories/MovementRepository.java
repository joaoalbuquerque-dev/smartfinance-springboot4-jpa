package com.smartfinance.repositories;

import com.smartfinance.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_User_Id(Long userId);

    Optional<Movement> findByIdAndAccount_User_Id(Long id, Long userId);
}
