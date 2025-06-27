package com.example.pfm.dto.goal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalResponse(
        Long id,
        String goalName,
        BigDecimal targetAmount,
        LocalDate targetDate,
        LocalDate startDate,
        BigDecimal currentProgress,
        double progressPercentage,
        BigDecimal remainingAmount) { }
