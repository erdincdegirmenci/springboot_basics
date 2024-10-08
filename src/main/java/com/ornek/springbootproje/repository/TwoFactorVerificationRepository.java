package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.TwoFactorVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwoFactorVerificationRepository extends JpaRepository<TwoFactorVerification, Long> {

    TwoFactorVerification save(TwoFactorVerification twoFactorVerification);
    @Query(name = "TwoFactorVerification.findByUserId")
    List<TwoFactorVerification> findByUserId(Long userId);
    @Query(name = "TwoFactorVerification.findByUserIdAndCode")
    TwoFactorVerification findByUserIdAndCode(Long userId, String code);
}