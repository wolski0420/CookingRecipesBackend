package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Category;
import com.app.cooking.recipes.backend.model.CategoryForm;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {
    private static final String collectionName = "categories";

    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();

        FirestoreClient.getFirestore().collection(collectionName).listDocuments().forEach(documentReference -> {
            try {
                Optional.ofNullable(documentReference.get().get().toObject(Category.class))
                        .ifPresent(category -> {
                            category.setDocumentId(documentReference.getId());
                            categories.add(category);
                        });
            } catch (InterruptedException | ExecutionException ignored) {}
        });

        return categories;
    }

    public Optional<Category> getById(String id) {
        try {
            return Optional.ofNullable(FirestoreClient.getFirestore().collection(collectionName).document(id).get().get().toObject(Category.class))
                    .map(category -> {
                        category.setDocumentId(id);
                        return category;
                    });
        } catch (ExecutionException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public void saveNew(CategoryForm categoryForm) {
        FirestoreClient.getFirestore().collection(collectionName).add(categoryForm);
    }
}
