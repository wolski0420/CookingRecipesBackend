package com.app.cooking.recipes.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class CookingRecipesBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CookingRecipesBackendApplication.class, args);
    }

    @PostConstruct
    public void initializeFirebase() {
        try {
            Logger logger = LoggerFactory.getLogger(CookingRecipesBackendApplication.class);

            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
            InputStream inputStream;
            if (serviceAccount.exists()) {
                inputStream = serviceAccount.getInputStream();
                logger.info("Location of json file - " + serviceAccount.getURL());
            } else {
                File file = new File("/mo-data/serviceAccountKey.json");
                logger.info("Location of json file - " + file.getAbsolutePath());
                inputStream = new FileInputStream(file);
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl("https://cookingrecipes-793c3.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options, "cookingRecipesBackend");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
