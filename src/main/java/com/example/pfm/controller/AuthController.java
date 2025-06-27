package com.example.pfm.controller;

import com.example.pfm.dto.auth.RegisterRequest;
import com.example.pfm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {

    private final AuthService svc;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
        return ResponseEntity.status(201).body(svc.register(req));
    }
}
