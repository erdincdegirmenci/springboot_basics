package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) throws Exception {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }
}