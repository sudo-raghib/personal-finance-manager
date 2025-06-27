package com.example.pfm.dto.goal;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalRequest(
        @NotBlank String goalName,
        @Positive  BigDecimal targetAmount,
        @Future LocalDate targetDate,
        @PastOrPresent LocalDate startDate

) { }
