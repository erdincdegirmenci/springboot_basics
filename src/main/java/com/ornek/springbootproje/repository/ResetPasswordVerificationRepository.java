package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.ResetPasswordVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordVerificationRepository extends JpaRepository<ResetPasswordVerification, Long> {

    ResetPasswordVerification save(ResetPasswordVerification resetPasswordVerification);
    @Query(name = "ResetPasswordVerification.findByUserId")
    List<ResetPasswordVerification> findByUserId(Long userId);
    @Query(name = "ResetPasswordVerification.findByUrl")
    ResetPasswordVerification findByUrl(String url);
}