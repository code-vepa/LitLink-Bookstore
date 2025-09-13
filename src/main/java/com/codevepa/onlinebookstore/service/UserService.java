package com.codevepa.onlinebookstore.service;

import com.codevepa.onlinebookstore.model.Users;
import com.codevepa.onlinebookstore.repository.UsersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UsersRepo repo;
    private final JwtService jwtService;

    public UserService(UsersRepo repo, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.repo = repo;
        this.jwtService = jwtService;
    }


    public ResponseEntity<?> verify(Users user) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            if (auth.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful");
                // Get the role of the user
                Optional<Users> found = repo.findByUsername(user.getUsername());
                if (found.isPresent()) {
                    String role = found.get().getRole();
                    return ResponseEntity.ok("Login successful as " + role + " " + response.get("token"));
                }
                return ResponseEntity.ok("Login successful");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    public ResponseEntity<?> register(Users user) {
        try {
            if (repo.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            if (user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fill out all the fields");
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            repo.save(user);
            return ResponseEntity.ok("Successfully registered as " + user.getRole());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
