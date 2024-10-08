package com.ornek.springbootproje.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "useraudits", schema = "public")
@NamedQuery(name = "UserAudit.findByUserId", query = "SELECT u FROM UserAudit u WHERE u.userId = :userId")
public class UserAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long auditId;
    private String device;
    private String ipAddress;
    private Date createDate;
}