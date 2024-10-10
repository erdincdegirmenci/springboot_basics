package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "roles", schema = "public")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;
}