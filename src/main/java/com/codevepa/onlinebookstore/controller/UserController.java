package com.codevepa.onlinebookstore.controller;

import com.codevepa.onlinebookstore.model.Users;
import com.codevepa.onlinebookstore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return service.register(user);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        return service.verify(user);
    }

}
