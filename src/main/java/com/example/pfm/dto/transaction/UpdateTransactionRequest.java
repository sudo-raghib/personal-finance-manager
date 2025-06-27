package com.example.pfm.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateTransactionRequest(
        @Positive BigDecimal amount,
        String description) { }
