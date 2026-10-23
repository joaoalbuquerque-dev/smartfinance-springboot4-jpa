package com.smartfinance.dto;

import com.smartfinance.entities.Installment;
import com.smartfinance.entities.enums.InstallmentStatus;

import java.time.LocalDate;

public record InstallmentResponseDTO(Long id, Integer number, Double amount,
                                     LocalDate dueDate, InstallmentStatus status) {

    public InstallmentResponseDTO(Installment installment) {
        this(installment.getId(), installment.getNumber(), installment.getAmount(),
                installment.getDueDate(), installment.getStatus());
    }
}
