package com.example.pfm.service;

import com.example.pfm.dto.category.CategoryRequest;
import com.example.pfm.dto.category.CategoryResponse;
import com.example.pfm.entity.Category;
import com.example.pfm.entity.User;
import com.example.pfm.exceptions.BadRequestException;
import com.example.pfm.exceptions.ConflictException;
import com.example.pfm.exceptions.NotFoundException;
import com.example.pfm.repository.CategoryRepository;
import com.example.pfm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;
    private final TransactionRepository txRepo;

    public List<CategoryResponse> list(User user) {
        return repo.findByUserOrUserIsNull(user).stream()
                .map(this::map)
                .toList();
    }

    public CategoryResponse create(CategoryRequest r, User u) {

        if (repo.findByNameAndUserOrDefault(r.name(), u).isPresent())
            throw new ConflictException("Duplicate category");

        Category c = Category.builder()
                .user(u)
                .name(r.name())
                .type(r.type())
                .custom(true)
                .build();
        repo.save(c);
        return map(c);
    }

    public void delete(String name, User u) {
        Category c = repo.findByNameAndUserOrDefault(name, u)
                .orElseThrow(() -> new NotFoundException("Category"));

        if (!c.isCustom())                  throw new BadRequestException("Default category");
        if (txRepo.countByCategoryAndDeletedFalse(c) > 0)
            throw new BadRequestException("Category in use");

        repo.delete(c);
    }

    private CategoryResponse map(Category c){
        return new CategoryResponse(c.getName(),
                c.getType().name(),
                c.isCustom());
    }


}
