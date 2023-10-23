package com.app.cooking.recipes.backend.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    default void clearLastDBDump(String prefix) {
        Optional.of(new File("."))
                .map(dir -> dir.listFiles((file, name) -> name.contains(prefix)))
                .stream()
                .flatMap(Arrays::stream)
                .forEach(File::delete);
    }
}
