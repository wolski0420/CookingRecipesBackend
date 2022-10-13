package com.app.cooking.recipes.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@SpringBootApplication
public class CookingRecipesBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CookingRecipesBackendApplication.class, args);
    }

    @PostConstruct
    public void initializeFirebase() {
        try {
            ClassLoader classLoader = CookingRecipesBackendApplication.class.getClassLoader();
            URL credentialsUrl = classLoader.getResource("serviceAccountKey.json");

            String urlForFile = Optional.ofNullable(credentialsUrl)
                    .map(URL::getFile)
                    .orElse("/mo-data/serviceAccountKey.json");

            File file = new File(urlForFile);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
