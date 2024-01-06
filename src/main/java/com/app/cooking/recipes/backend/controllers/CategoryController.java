package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.Category;
import com.app.cooking.recipes.backend.model.CategoryForm;
import com.app.cooking.recipes.backend.model.CategoryType;
import com.app.cooking.recipes.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostConstruct
    private void loadDefaultCategories() {
        if (categoryService.getAll().isEmpty()) {
            categoryService.saveNew(new CategoryForm("Ciasta", CategoryType.SWEET));
            categoryService.saveNew(new CategoryForm("Desery", CategoryType.SWEET));
            categoryService.saveNew(new CategoryForm("Zupy", CategoryType.MEAL));
            categoryService.saveNew(new CategoryForm("Kurczak", CategoryType.MEAL));
            categoryService.saveNew(new CategoryForm("Ryba", CategoryType.MEAL));
            categoryService.saveNew(new CategoryForm("Sałatki", CategoryType.SNACK));
            categoryService.saveNew(new CategoryForm("Mięsne", CategoryType.SNACK));
            categoryService.saveNew(new CategoryForm("Sosy", CategoryType.OTHERS));
        }
    }

    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryService.getAll();
    }
}
