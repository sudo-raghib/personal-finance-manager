package com.example.pfm.service;

import com.example.pfm.dto.report.*;
import com.example.pfm.entity.Category;
import com.example.pfm.entity.Transaction;
import com.example.pfm.entity.User;
import com.example.pfm.exceptions.BadRequestException;
import com.example.pfm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository txRepo;

    public MonthlyReportResponse monthly(User u, int y, int m){

        if(m<1||m>12) throw new BadRequestException("Invalid month");

        LocalDate start = LocalDate.of(y,m,1);
        LocalDate end   = start.withDayOfMonth(start.lengthOfMonth());

        Map<String, BigDecimal> income  = categoryTotals(u, Category.Type.INCOME, start, end);
        Map<String, BigDecimal> expense = categoryTotals(u, Category.Type.EXPENSE, start, end);

        BigDecimal in  = income.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal out = expense.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyReportResponse(y,m,income,expense,in.subtract(out));
    }

    public YearlyReportResponse yearly(User u, int y){
        LocalDate s = LocalDate.of(y,1,1);
        LocalDate e = s.withMonth(12).withDayOfMonth(31);

        Map<String, BigDecimal> income  = categoryTotals(u, Category.Type.INCOME, s, e);
        Map<String, BigDecimal> expense = categoryTotals(u, Category.Type.EXPENSE, s, e);

        BigDecimal in  = income.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal out = expense.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        return new YearlyReportResponse(y,income,expense,in.subtract(out));
    }

    private Map<String,BigDecimal> categoryTotals(User u, Category.Type type,
                                                  LocalDate s, LocalDate e){
        return txRepo.filter(u,s,e,null,type).stream()      // already excludes deleted
                .collect(Collectors.groupingBy(t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount, BigDecimal::add)));
    }

}
