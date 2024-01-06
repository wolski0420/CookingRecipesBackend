package com.app.cooking.recipes.backend.controllers;

import com.app.cooking.recipes.backend.model.User;
import com.app.cooking.recipes.backend.model.UserForm;
import com.app.cooking.recipes.backend.model.UserRole;
import com.app.cooking.recipes.backend.services.UserService;
import lombok.*;
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

    @PutMapping("/role")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRoleReq userRoleReq) {
        Optional<User> userInstanceById = userService.getById(userRoleReq.getDocumentId());
        if (userInstanceById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with given ID does not exist!");
        }

        User user = userInstanceById.get();
        user.setUserRole(userRoleReq.getUserRole());

        userService.updateExisting(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/data")
    public ResponseEntity<?> updateUserData(@RequestBody UserDataReq userDataReq, Principal loggedUser) {
        Optional<User> loggedInUser = userService.getByUsername(loggedUser.getName());
        if (loggedInUser.isEmpty() || !loggedInUser.get().getDocumentId().equals(userDataReq.documentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update different user data!");
        }

        Optional<User> userInstanceById = userService.getById(userDataReq.getDocumentId());
        if (userInstanceById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with given ID does not exist!");
        }

        Optional<User> userInstanceByUserName = userService.getByUsername(userDataReq.getUsername());
        if (userInstanceByUserName.isPresent() && !userInstanceByUserName.get().getDocumentId().equals(userDataReq.getDocumentId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with proposed username already exists!");
        }

        User user = userInstanceById.get();
        user.setUsername(userDataReq.getUsername());
        user.setPassword(passwordEncoder.encode(userDataReq.getPassword()));

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDataReq {
        private String documentId;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleReq {
        private String documentId;
        private UserRole userRole;
    }
}
