package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.entities.User;
import com.ornek.springbootproje.service.AuditService;
import com.ornek.springbootproje.service.AuthService;
import com.ornek.springbootproje.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService, AuditService auditService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody User user) throws Exception {
        String token = authService.Login(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(token);
    }
}