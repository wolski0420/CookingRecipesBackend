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
import java.io.IOException;

@SpringBootApplication
public class CookingRecipesBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CookingRecipesBackendApplication.class, args);
    }

    @PostConstruct
    public void initializeFirebase() {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");

            Logger logger = LoggerFactory.getLogger(CookingRecipesBackendApplication.class);
            logger.info(serviceAccount.getURL().toString());
            logger.info(serviceAccount.getURI().toString());

            if (!serviceAccount.exists()) {
                serviceAccount = new ClassPathResource("../../../../../mo-data/serviceAccountKey.json");
                logger.info("if: " + serviceAccount.getURL());
                logger.info("if: " + serviceAccount.getURI());
            }

            logger.info(serviceAccount.getURL().toString());
            logger.info(serviceAccount.getURI().toString());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setDatabaseUrl("https://cookingrecipes-793c3.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options, "cookingRecipes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
