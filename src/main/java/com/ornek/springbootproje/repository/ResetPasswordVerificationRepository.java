package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.ResetPasswordVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordVerificationRepository extends JpaRepository<ResetPasswordVerification, Long> {

    ResetPasswordVerification save(ResetPasswordVerification resetPasswordVerification);
    List<ResetPasswordVerification> findByUserid(Long userid);
    Optional<ResetPasswordVerification> findByUrl(String url);
}