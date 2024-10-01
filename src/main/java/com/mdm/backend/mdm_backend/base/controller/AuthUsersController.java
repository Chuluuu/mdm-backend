package com.mdm.backend.mdm_backend.base.controller;

import com.mdm.backend.mdm_backend.base.DTO.users.CreateUserDTO;
import com.mdm.backend.mdm_backend.base.DTO.users.UserUpdateDTO;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.repository.AuthUsersRepository;
import com.mdm.backend.mdm_backend.base.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/users")
public class AuthUsersController {
    @Autowired
    private final AuthService authService;

    private final AuthUsersRepository authUsersRepository;  // Repository for handling CRUD

    public AuthUsersController(AuthService authService,  AuthUsersRepository authUsersRepository) {
        this.authService = authService;
        this.authUsersRepository = authUsersRepository;
    }
    // 1. Create a new user
    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO userRequest) {
        try {
            AuthUsers newUser = authService.create(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully with ID: " + newUser.getId());
        } catch (Exception e) {
            // Handle exceptions and return error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
    }

    // 2. Get all users
    @GetMapping
    public ResponseEntity<List<AuthUsers>> getAllUsers() {
        List<AuthUsers> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 3. Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthUsers> getUserById(@PathVariable Long id) {
        return authService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 4. Update a user
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updatedUserRequest) {
        return authService.update(id, updatedUserRequest)
                .map(user -> ResponseEntity.ok("User updated successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    // 5. Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return authService.delete(id)
                .map(user -> ResponseEntity.ok("User deleted successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    // 6. Get current user info
    @GetMapping("/current")
    public ResponseEntity<AuthUsers> getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthUsers) {
                AuthUsers loggedInUser = (AuthUsers) principal;
                return ResponseEntity.ok(loggedInUser);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}


