package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "twofactorverifications", schema = "public")
@NamedQuery(name = "TwoFactorVerification.findByUserId", query = "SELECT t FROM TwoFactorVerification t WHERE t.userId = :userId")
@NamedQuery(name = "TwoFactorVerification.findByUserIdAndCode", query = "SELECT t  FROM TwoFactorVerification t WHERE t.userId = :userId and t.code = :code")
public class TwoFactorVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;
    String code;
    LocalDateTime expirationDate;

}