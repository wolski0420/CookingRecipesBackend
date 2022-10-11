package com.app.cooking.recipes.backend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeForm {
    private String name;
    private List<String> ingredients;
    private String description;
}
