package com.smartfinance.repositories;

import com.smartfinance.entities.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    Optional<Installment> findByIdAndMovement_Account_User_Id(Long installmentId, Long userId);
}
