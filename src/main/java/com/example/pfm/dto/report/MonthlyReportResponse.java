package com.example.pfm.dto.report;

import java.math.BigDecimal;
import java.util.Map;

public record MonthlyReportResponse(
        int year, int month,
        Map<String, BigDecimal> totalIncome,
        Map<String, BigDecimal> totalExpenses,
        BigDecimal netSavings) { }
