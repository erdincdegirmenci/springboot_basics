package com.ornek.springbootproje.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "userroles", schema = "public")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userid;
    Integer roleid;

}