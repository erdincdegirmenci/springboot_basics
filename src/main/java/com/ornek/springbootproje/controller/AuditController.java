package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.entities.UserAudit;
import com.ornek.springbootproje.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    // Kullanıcı aktivitesi ekleme
    @PostMapping
    public ResponseEntity<UserAudit> AddUserAudit(@RequestBody UserAudit userAudit) {
        UserAudit savedAudit = auditService.AddUserAudit(userAudit);
        return ResponseEntity.ok(savedAudit);
    }

    // Kullanıcı ID'sine göre aktiviteleri alma
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAudit>> GetUserAuditsByUserId(@PathVariable Long userId) {
        List<UserAudit> userAudits = auditService.GetUserAuditsByUserId(userId);
        return ResponseEntity.ok(userAudits);
    }
}