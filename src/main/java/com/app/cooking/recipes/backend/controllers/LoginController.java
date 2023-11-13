package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.LoginRequest;
import com.app.cooking.recipes.backend.model.User;
import com.app.cooking.recipes.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/login", produces = "application/json")
@CrossOrigin(origins = "*")
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Optional<User> basicLogin(@RequestBody LoginRequest loginRequest) {
        return userService.getByUsername(loginRequest.getUsername())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
    }
}
