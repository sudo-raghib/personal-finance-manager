package com.example.pfm.dto.category;

import com.example.pfm.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name,
        Category.Type type
) {}
