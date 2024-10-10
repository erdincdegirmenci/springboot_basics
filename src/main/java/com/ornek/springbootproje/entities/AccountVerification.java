package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accountverifications", schema = "public")
@NamedQuery(name = "AccountVerification.findByUserId",query = "SELECT a FROM AccountVerification a WHERE a.userid = :userid")
@NamedQuery(name = "AccountVerification.findByUrl", query = "SELECT a FROM AccountVerification a WHERE a.url = :url")
public class AccountVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userid;
    String url;
    LocalDateTime createdat;
}