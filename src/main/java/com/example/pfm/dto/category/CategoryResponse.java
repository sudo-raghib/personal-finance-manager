package com.example.pfm.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryResponse(
        String  name,
        String  type,
        @JsonProperty("custom") boolean custom
) { }
