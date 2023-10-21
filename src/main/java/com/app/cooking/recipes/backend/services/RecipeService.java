package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Recipe;
import com.app.cooking.recipes.backend.model.RecipeForm;
import com.app.cooking.recipes.backend.utils.LocalRepository;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class RecipeService implements LocalRepository {
    private static final String collectionName = "recipes";

    public Optional<Recipe> getById(String id) {
        try {
            return Optional.ofNullable(FirestoreClient.getFirestore().collection(collectionName).document(id).get().get().toObject(Recipe.class))
                    .map(recipe -> {
                        recipe.setDocumentId(id);
                        return recipe;
                    });
        } catch (ExecutionException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();

        FirestoreClient.getFirestore().collection(collectionName).listDocuments().forEach(documentReference -> {
            try {
                Optional.ofNullable(documentReference.get().get().toObject(Recipe.class))
                        .ifPresent(recipe -> {
                            recipe.setDocumentId(documentReference.getId());
                            recipes.add(recipe);
                        });
            } catch (InterruptedException | ExecutionException ignored) {}
        });

        return recipes;
    }

    public Optional<Recipe> getByName(String name) {
        return getAll().stream()
                .filter(recipe -> recipe.getName().equals(name))
                .findAny();
    }

    public void saveNew(RecipeForm recipeForm) {
        FirestoreClient.getFirestore().collection(collectionName).add(recipeForm);
    }

    public void updateExisting(Recipe recipe) {
        FirestoreClient.getFirestore().collection(collectionName).document(recipe.getDocumentId())
                .update("name", recipe.getName(),
                        "categoryId", recipe.getCategoryId(),
                        "imageUrl", recipe.getImageUrl(),
                        "description", recipe.getDescription(),
                        "ingredients", recipe.getIngredients(),
                        "ingredientGroups", recipe.getIngredientGroups(),
                        "creationDate", recipe.getCreationDate());
    }

    public void delete(Recipe recipe) {
        FirestoreClient.getFirestore().collection(collectionName).document(recipe.getDocumentId()).delete();
    }

    @PostConstruct
    private void init() {
        dumpDBToJson("recipes_onInit", getAll());
    }

    @PreDestroy
    private void destroy() {
        dumpDBToJson("recipes_onDestroy", getAll());
    }
}
