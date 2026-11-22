package com.smartfinance.repositories;

import com.smartfinance.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInstallment_Movement_Account_User_Id(Long userId);

    Optional<Payment> findByIdAndInstallment_Movement_Account_User_Id(Long id, Long userId);
}
