package com.example.pfm.repository;

import com.example.pfm.entity.Category;
import com.example.pfm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByUserOrUserIsNull(User user);

    @Query("""
           select c from Category c
           where c.name = :name
             and (c.user = :user or c.user is null)
           """)
    Optional<Category> findByNameAndUserOrDefault(String name, User user);

    boolean existsByNameAndUser(String name, User user);
}
