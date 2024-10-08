package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuditRepository extends JpaRepository<UserAudit, Long> {

    UserAudit save(UserAudit userAudit);
    List<UserAudit> findAll();
    @Query(name = "UserAudit.findByUserId")
    List<UserAudit> findByUserId(Long userId);
}