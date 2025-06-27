package com.example.pfm.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        LocalDate date,
        String category,
        String description,
        String type) { }
