package com.example.pfm.config;

import com.example.pfm.entity.Category;
import com.example.pfm.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Stream;

@Component @RequiredArgsConstructor
public class BootstrapDefaults {

    private final CategoryRepository repo;

    @PostConstruct
    void init(){
        if(repo.count() > 0) return;
        Stream.of("Salary","Food","Rent","Transportation","Entertainment","Healthcare","Utilities")
                .forEach(this::add);
    }
    private void add(String name){
        Category.Type type = name.equals("Salary") ? Category.Type.INCOME : Category.Type.EXPENSE;
        repo.save(Category.builder().name(name).type(type).custom(false).user(null).build());
    }
}
