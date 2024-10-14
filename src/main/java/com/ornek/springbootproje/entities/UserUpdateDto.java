package com.ornek.springbootproje.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateDto {
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