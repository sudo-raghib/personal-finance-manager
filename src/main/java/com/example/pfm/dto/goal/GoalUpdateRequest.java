package com.example.pfm.dto.goal;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalUpdateRequest(
        @Positive BigDecimal targetAmount,
        @Future   LocalDate   targetDate
) { }
