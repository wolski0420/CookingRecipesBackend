package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.Category;
import com.app.cooking.recipes.backend.model.CategoryForm;
import com.app.cooking.recipes.backend.utils.LocalRepository;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryService implements LocalRepository {
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

    @PostConstruct
    private void init() {
        dumpDBToJson("categories_onInit", getAll());
    }

    @Scheduled(initialDelayString = "${categories.db.dump.initial.delay.seconds:21600}",
            fixedRateString = "${categories.db.dump.fixed.rate.seconds:21600}",
            timeUnit = TimeUnit.SECONDS)
    private void cronDump() {
        clearLastDBDump("categories_cronDump");
        dumpDBToJson("categories_cronDump", getAll());
    }

    @PreDestroy
    private void destroy() {
        dumpDBToJson("categories_onDestroy", getAll());
    }
}
