package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "resetpasswordverifications", schema = "public")
public class ResetPasswordVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userid;
    String url;
    LocalDateTime expirationdate;
}