package com.app.cooking.recipes.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Category")
public class Category {
    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
}
