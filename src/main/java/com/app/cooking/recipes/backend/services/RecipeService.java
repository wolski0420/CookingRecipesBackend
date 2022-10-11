package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Recipe;
import com.app.cooking.recipes.backend.model.RecipeForm;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class RecipeService {
    private static final String collectionName = "recipes";

    public Optional<Recipe> getById(String id) {
        Recipe recipeToReturn = null;

        try {
            recipeToReturn = FirestoreClient.getFirestore().collection(collectionName).document(id).get().get().toObject(Recipe.class);
            recipeToReturn.setDocumentId(id);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(recipeToReturn);
    }

    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();

        FirestoreClient.getFirestore().collection(collectionName).listDocuments().forEach(documentReference -> {
            try {
                Recipe recipeToPrint = documentReference.get().get().toObject(Recipe.class);
                recipeToPrint.setDocumentId(documentReference.getId());
                recipes.add(recipeToPrint);
                documentReference.getId();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
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
                        "description", recipe.getDescription(),
                        "ingredients", recipe.getIngredients());
    }

    public void delete(Recipe recipe) {
        FirestoreClient.getFirestore().collection(collectionName).document(recipe.getDocumentId()).delete();
    }
}
