package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.Category;
import com.app.cooking.recipes.backend.services.CategoryService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveNewCategory(@RequestBody CategoryData categoryData) {
        if (categoryService.getByName(categoryData.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category with given name already exists!");
        }

        categoryService.saveNewOrUpdateExisting(Category.builder()
                .name(categoryData.getName())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryData categoryData) {
        final UUID id = UUID.fromString(categoryData.getId());
        if (categoryService.getById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Category with given ID does not exist!");
        }

        categoryService.saveNewOrUpdateExisting(Category.builder()
                .id(id)
                .name(categoryData.getName())
                .build());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public Iterable<CategoryData> getAllCategories() {
        return categoryService.getAll().stream()
                .map(category -> CategoryData.builder()
                        .id(category.getId().toString())
                        .name(category.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryData categoryData) {
        final UUID uuid = UUID.fromString(categoryData.getId());
        Optional<Category> categoryOptional = categoryService.getById(uuid);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Category with given ID does not exist!");
        }

        categoryService.delete(categoryOptional.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Data
    @Builder
    static class CategoryData {
        private String id = "";
        private String name;
    }
}
