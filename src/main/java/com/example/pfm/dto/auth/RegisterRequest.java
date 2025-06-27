package com.example.pfm.dto.auth;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotNull String username,
        @Size(min = 6) String password,
        @NotBlank String fullName,
        @NotBlank String phoneNumber) { }
