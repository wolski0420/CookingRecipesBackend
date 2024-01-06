package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Recipe;
import com.app.cooking.recipes.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {
    private final RecipeRepository repository;

    @Autowired
    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public Optional<Recipe> getById(UUID id) {
        return repository.findById(id);
    }

    public List<Recipe> getAll() {
        return repository.findAll();
    }

    public Optional<Recipe> getByName(String name) {
        return repository.findByName(name);
    }

    public void saveNewOrUpdateExisting(Recipe recipe) {
        repository.saveAndFlush(recipe);
    }

    public void delete(Recipe recipe) {
        repository.delete(recipe);
    }
}
