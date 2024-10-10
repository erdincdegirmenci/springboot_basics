package com.ornek.springbootproje.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rolepermissions", schema = "public")
public class RolePermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long roleid;
    int permissionid;

}