package com.app.cooking.recipes.backend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private String documentId;
    private String categoryId;
    private String name;
    private String imageUrl;
    private List<String> ingredients;
    private List<IngredientGroup> ingredientGroups;
    private String description;
    private String creationDate;
}
