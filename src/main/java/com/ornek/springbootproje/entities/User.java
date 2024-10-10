package com.ornek.springbootproje.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users", schema = "public")
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Firstname is required")
    String firstname;
    @NotBlank(message = "Lastname is required")
    String lastname;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
    String address;
    String phone;
    String title;
    String bio;
    Boolean isactive = true;
    Boolean islock = false;
    Boolean isusingmfa = false;
    String imageurl;

    LocalDateTime createdate = LocalDateTime.now();
    LocalDateTime updatedate = LocalDateTime.now();

    String createuser;
    String lastupdateuser;
    Integer failedloginattemp;
}