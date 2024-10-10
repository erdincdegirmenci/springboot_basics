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
    Long id;

    Long userId;
    Long auditId;
    String device;
    String ipAddress;
    Date createDate;
}