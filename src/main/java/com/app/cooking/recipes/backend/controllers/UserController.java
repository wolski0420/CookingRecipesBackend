package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.User;
import com.app.cooking.recipes.backend.model.UserForm;
import com.app.cooking.recipes.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> saveNewUser(@RequestBody UserForm userForm) {
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));

        if (userService.getByUsername(userForm.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with given username already exists!");
        }

        userService.saveNew(userForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user, Principal loggedUser) {
        Optional<User> loggedInUser = userService.getByUsername(loggedUser.getName());
        if (loggedInUser.isEmpty() || !loggedInUser.get().getDocumentId().equals(user.getDocumentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update different user!");
        }

        Optional<User> userInstanceById = userService.getById(user.getDocumentId());
        if (userInstanceById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with given ID does not exist!");
        }

        Optional<User> userInstanceByUserName = userService.getByUsername(user.getUsername());
        if (userInstanceByUserName.isPresent() && !userInstanceByUserName.get().getDocumentId().equals(user.getDocumentId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with proposed username already exists!");
        }

        if (!userInstanceById.get().getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.updateExisting(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.getAll();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        if (userService.getById(user.getDocumentId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with given ID does not exist!");
        }

        userService.delete(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
