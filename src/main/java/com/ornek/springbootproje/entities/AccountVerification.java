package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accountverifications", schema = "public")
@NamedQuery(name = "AccountVerification.findByUserId",query = "SELECT a FROM AccountVerification a WHERE a.userId = :userId")
@NamedQuery(name = "AccountVerification.findByUrl", query = "SELECT a FROM AccountVerification a WHERE a.url = :url")
public class AccountVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String url;
    private LocalDateTime createdAt;
}