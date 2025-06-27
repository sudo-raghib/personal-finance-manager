package com.example.pfm.dto.transaction;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @Positive BigDecimal amount,
        @PastOrPresent LocalDate date,
        @NotBlank String category,
        @Size(max = 255) String description) {
}
