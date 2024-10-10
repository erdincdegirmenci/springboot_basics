package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.entities.Role;
import com.ornek.springbootproje.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    // Kullanıcının rolünü ID ile alma
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Role>> getRolesByUserId(@PathVariable Long userId) {
//        List<Role> roles = roleService.findByUserId(userId);
//        return ResponseEntity.ok(roles);
//    }
//
    // Tüm rolleri alma
    @GetMapping
    public ResponseEntity<List<Role>> GetAllRoles() {
        List<Role> roles = roleService.GetAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Rolleri izinlerle birlikte alma
//    @GetMapping("/permissions")
//    public ResponseEntity<List<Role>> getRolesWithPermissions() {
//        List<Role> roles = roleService.findRolesWithPermissions();
//        return ResponseEntity.ok(roles);
//    }
//
//    // Rol ID'sine göre izinleri alma
//    @GetMapping("/{roleId}/permissions")
//    public ResponseEntity<List<Role>> getPermissionsByRoleId(@PathVariable Long roleId) {
//        List<Role> permissions = roleService.getPermissionsByRoleId(roleId);
//        return ResponseEntity.ok(permissions);
//    }
}
