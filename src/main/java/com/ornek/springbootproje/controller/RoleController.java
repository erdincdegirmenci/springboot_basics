package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.entities.Role;
import com.ornek.springbootproje.entities.UserRole;
import com.ornek.springbootproje.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Yeni rol ekleme
    @PostMapping
    public ResponseEntity<Role> AddRole(@RequestBody Role role) {
        Role savedRole = roleService.AddRole(role);
        return ResponseEntity.ok(savedRole);
    }

    // Kullanıcının rolünü ID ile alma
    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<UserRole>> getRolesByUserId(@PathVariable Long userId) {
        Optional<UserRole> userRoles = roleService.findByUserId(userId);
        return ResponseEntity.ok(userRoles);
    }

    // Tüm rolleri alma
    @GetMapping
    public ResponseEntity<List<Role>> GetAllRoles() {
        List<Role> roles = roleService.GetAllRoles();
        return ResponseEntity.ok(roles);
    }
}
