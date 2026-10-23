package com.smartfinance.dto;

import java.time.LocalDate;

public record InstallmentRequestDTO(Integer number, LocalDate dueDate) {
}
