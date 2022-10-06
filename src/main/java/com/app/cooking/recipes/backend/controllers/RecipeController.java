package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.Recipe;
import com.app.cooking.recipes.backend.services.RecipeService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe", produces = "application/json")
@CrossOrigin(origins = "*")
public class RecipeController {
    private static final String ingredientsDelimiter = ",";
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveNewRecipe(@RequestBody RecipeData recipeData) {
        if (recipeService.getByName(recipeData.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe with given name already exists!");
        }

        recipeService.saveNewOrUpdateExisting(Recipe.builder()
                .name(recipeData.getName())
                .ingredients(String.join(ingredientsDelimiter, recipeData.getIngredients()))
                .description(recipeData.getDescription())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updateRecipe(@RequestBody RecipeData recipeData) {
        final UUID id = UUID.fromString(recipeData.getId());
        if (recipeService.getById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Recipe with given ID does not exist!");
        }

        recipeService.saveNewOrUpdateExisting(Recipe.builder()
                .id(id)
                .name(recipeData.getName())
                .ingredients(String.join(ingredientsDelimiter, recipeData.getIngredients()))
                .description(recipeData.getDescription())
                .build());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public Iterable<RecipeData> getAllRecipes() {
        return recipeService.getAll().stream()
                .map(recipe -> RecipeData.builder()
                        .id(recipe.getId().toString())
                        .name(recipe.getName())
                        .ingredients(Arrays.asList(recipe.getIngredients().split(ingredientsDelimiter)))
                        .description(recipe.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRecipe(@RequestBody RecipeData recipeData) {
        final UUID uuid = UUID.fromString(recipeData.getId());
        Optional<Recipe> recipeOptional = recipeService.getById(uuid);
        if (recipeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Recipe with given ID does not exist!");
        }

        recipeService.delete(recipeOptional.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Data
    @Builder
    static class RecipeData {
        private String id = "";
        private String name;
        private List<String> ingredients;
        private String description;
    }
}
