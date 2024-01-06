package com.app.cooking.recipes.backend.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String documentId;
    private String username;
    private String password;
    private UserRole userRole;
}
