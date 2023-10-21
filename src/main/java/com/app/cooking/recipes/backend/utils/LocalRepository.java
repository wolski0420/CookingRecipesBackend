package com.app.cooking.recipes.backend.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface LocalRepository {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    default void dumpDBToJson(String prefix, List<?> objects) {
        try (FileWriter writer = new FileWriter(
                String.format("%s_%s.json", prefix, LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                ))
        )) {
            gson.toJson(objects, writer);
        } catch (IOException e) {
            System.out.println(gson.toJson(objects));
            throw new RuntimeException(e);
        }
    }
}
