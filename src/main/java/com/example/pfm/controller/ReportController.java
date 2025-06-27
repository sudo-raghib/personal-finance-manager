package com.example.pfm.controller;

import com.example.pfm.dto.report.*;
import com.example.pfm.entity.User;
import com.example.pfm.service.ReportService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/reports") @RequiredArgsConstructor
public class ReportController {

    private final ReportService svc;

    @GetMapping("/monthly/{year}/{month}")
    public MonthlyReportResponse monthly(@PathVariable int year,
                                         @PathVariable @Min(1) @Max(12) int month,
                                         @AuthenticationPrincipal User u){
        return svc.monthly(u,year,month);
    }

    @GetMapping("/yearly/{year}")
    public YearlyReportResponse yearly(@PathVariable int year,
                                       @AuthenticationPrincipal User u){
        return svc.yearly(u,year);
    }
}