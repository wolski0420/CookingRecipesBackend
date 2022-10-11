package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.Recipe;
import com.app.cooking.recipes.backend.model.RecipeForm;
import com.app.cooking.recipes.backend.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/recipe", produces = "application/json")
@CrossOrigin(origins = "*")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveNewRecipe(@RequestBody RecipeForm recipeForm) {
        if (recipeService.getByName(recipeForm.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe with given name already exists!");
        }

        recipeService.saveNew(recipeForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe recipe) {
        if (recipeService.getById(recipe.getDocumentId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Recipe with given ID does not exist!");
        }

        recipeService.updateExisting(recipe);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public Iterable<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRecipe(@RequestBody Recipe recipe) {
        Optional<Recipe> recipeOptional = recipeService.getById(recipe.getDocumentId());
        if (recipeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Recipe with given ID does not exist!");
        }

        recipeService.delete(recipeOptional.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
