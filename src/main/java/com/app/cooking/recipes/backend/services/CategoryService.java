package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Category;
import com.app.cooking.recipes.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Optional<Category> getById(UUID id) {
        return repository.findById(id);
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Optional<Category> getByName(String name) {
        return repository.findByName(name);
    }

    public void saveNewOrUpdateExisting(Category category) {
        repository.saveAndFlush(category);
    }

    public void delete(Category category) {
        repository.delete(category);
    }
}
