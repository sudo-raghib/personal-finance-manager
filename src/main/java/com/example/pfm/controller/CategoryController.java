package com.example.pfm.controller;

import com.example.pfm.dto.category.CategoryRequest;
import com.example.pfm.dto.category.CategoryResponse;
import com.example.pfm.entity.User;
import com.example.pfm.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService svc;

    @GetMapping
    public List<CategoryResponse> list(@AuthenticationPrincipal User u) {
        return svc.list(u);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req,
                                                   @AuthenticationPrincipal User u) {
        return ResponseEntity.status(201).body(svc.create(req, u));
    }

    @DeleteMapping("/{name}")
    public Map<String,String> delete(@PathVariable String name,
                                     @AuthenticationPrincipal User u) {
        svc.delete(name, u);
        return Map.of("message","Category deleted successfully");
    }
}
