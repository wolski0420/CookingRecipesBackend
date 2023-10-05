package com.app.cooking.recipes.backend.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String documentId;
    private String name;
    private CategoryType type;
}


