package com.example.pfm.controller;

import com.example.pfm.dto.goal.GoalRequest;
import com.example.pfm.dto.goal.GoalResponse;
import com.example.pfm.dto.goal.GoalUpdateRequest;
import com.example.pfm.entity.User;
import com.example.pfm.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService svc;

    @PostMapping
    public ResponseEntity<GoalResponse> create(
            @Valid @RequestBody GoalRequest req,
            @AuthenticationPrincipal User user) {

        GoalResponse body = svc.create(req, user);
        return ResponseEntity.status(201).body(body);
    }

    @GetMapping
    public List<GoalResponse> list(@AuthenticationPrincipal User user) {
        return svc.list(user);
    }

    @GetMapping("/{id}")
    public GoalResponse get(@PathVariable Long id,
                            @AuthenticationPrincipal User user) {
        return svc.get(id, user);
    }

    @PutMapping("/{id}")
    public GoalResponse update(@PathVariable Long id,
                               @Valid @RequestBody GoalUpdateRequest req,
                               @AuthenticationPrincipal User user) {

        return svc.update(id, req.targetAmount(), req.targetDate(), user);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id,
                                      @AuthenticationPrincipal User user) {
        svc.delete(id, user);
        return Map.of("message", "Goal deleted successfully");
    }
}
