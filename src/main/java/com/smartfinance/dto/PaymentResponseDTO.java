package com.smartfinance.dto;

import com.smartfinance.entities.Payment;

import java.time.Instant;

public record PaymentResponseDTO(Long id, Instant paidAt, Long installmentId) {

    public PaymentResponseDTO(Payment payment) {
        this(payment.getId(), payment.getPaidAt(), payment.getInstallment().getId());
    }
}
