package com.app.cooking.recipes.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@SpringBootApplication
public class CookingRecipesBackendApplication {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = CookingRecipesBackendApplication.class.getClassLoader();

        URL credentialsUrl = classLoader.getResource("serviceAccountKey.json");
        if (credentialsUrl == null) {
            credentialsUrl = classLoader.getResource("mo-data/serviceAccountKey.json");
        }

        File file = new File(Objects.requireNonNull(credentialsUrl).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://cookingrecipes-793c3.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options, "appName");
        } else {
            FirebaseApp.initializeApp(options);
        }

        SpringApplication.run(CookingRecipesBackendApplication.class, args);
    }

}
