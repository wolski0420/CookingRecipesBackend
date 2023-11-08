package com.app.cooking.recipes.backend.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private String username;
    private String password;
    private UserRole userRole;
}
