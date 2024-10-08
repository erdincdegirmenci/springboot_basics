package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "resetpasswordverifications", schema = "public")
@NamedQuery(name = "ResetPasswordVerification.findByUserId", query = "SELECT r FROM  ResetPasswordVerification  r WHERE r.userId = :userId")
@NamedQuery(name = "ResetPasswordVerification.findByUrl", query = "SELECT r FROM  ResetPasswordVerification  r WHERE r.url = :url")
public class ResetPasswordVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String url;
    private LocalDateTime expirationDate;
}