package com.app.cooking.recipes.backend.services;

import com.app.cooking.recipes.backend.model.User;
import com.app.cooking.recipes.backend.model.UserForm;
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
public class UserService implements LocalRepository {
    private static final String collectionName = "users";

    public Optional<User> getById(String id) {
        try {
            return Optional.ofNullable(FirestoreClient.getFirestore().collection(collectionName).document(id).get().get().toObject(User.class))
                    .map(user -> {
                        user.setDocumentId(id);
                        return user;
                    });
        } catch (ExecutionException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        FirestoreClient.getFirestore().collection(collectionName).listDocuments().forEach(documentReference -> {
            try {
                Optional.ofNullable(documentReference.get().get().toObject(User.class))
                        .ifPresent(user -> {
                            user.setDocumentId(documentReference.getId());
                            users.add(user);
                        });
            } catch (InterruptedException | ExecutionException ignored) {}
        });

        return users;
    }

    public Optional<User> getByUsername(String username) {
        return getAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    public void saveNew(UserForm userForm) {
        FirestoreClient.getFirestore().collection(collectionName).add(userForm);
    }

    public void updateExisting(User user) {
        FirestoreClient.getFirestore().collection(collectionName).document(user.getDocumentId())
                .update("username", user.getUsername(),
                        "password", user.getPassword(),
                        "userRole", user.getUserRole());
    }

    public void delete(User user) {
        FirestoreClient.getFirestore().collection(collectionName).document(user.getDocumentId()).delete();
    }

    @PostConstruct
    private void init() {
        dumpDBToJson("users_onInit", getAll());
    }

    @Scheduled(initialDelayString = "${users.db.dump.initial.delay.seconds:21600}",
            fixedRateString = "${users.db.dump.fixed.rate.seconds:21600}",
            timeUnit = TimeUnit.SECONDS)
    private void cronDump() {
        clearLastDBDump("users_cronDump");
        dumpDBToJson("users_cronDump", getAll());
    }

    @PreDestroy
    private void destroy() {
        dumpDBToJson("users_onDestroy", getAll());
    }
}
