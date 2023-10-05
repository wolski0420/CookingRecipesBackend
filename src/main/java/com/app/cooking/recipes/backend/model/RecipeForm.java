package com.app.cooking.recipes.backend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeForm {
    private String categoryId;
    private String name;
    private String imageUrl;
    private List<String> ingredients;
    private String description;
}
