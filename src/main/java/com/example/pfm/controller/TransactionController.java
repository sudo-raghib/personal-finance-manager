package com.example.pfm.controller;

import com.example.pfm.dto.transaction.CreateTransactionRequest;
import com.example.pfm.dto.transaction.TransactionResponse;
import com.example.pfm.dto.transaction.UpdateTransactionRequest;
import com.example.pfm.entity.Category;
import com.example.pfm.entity.User;
import com.example.pfm.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService svc;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody CreateTransactionRequest req,
            @AuthenticationPrincipal User user) {

        TransactionResponse body = svc.create(req, user);
        return ResponseEntity.status(201).body(body);
    }

    @GetMapping
    public List<TransactionResponse> list(
            @RequestParam(required = false) @PastOrPresent LocalDate startDate,
            @RequestParam(required = false) @PastOrPresent LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Category.Type type,
            @AuthenticationPrincipal User user) {

        return svc.filter(user, startDate, endDate, category, type);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionRequest req,
            @AuthenticationPrincipal User user) {

        return svc.update(id, req, user);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        svc.delete(id, user);
        return Map.of("message", "Transaction deleted successfully");
    }
}
