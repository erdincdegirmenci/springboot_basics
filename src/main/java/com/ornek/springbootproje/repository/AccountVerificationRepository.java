package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.AccountVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountVerificationRepository extends JpaRepository<AccountVerification, Long> {

    AccountVerification save(AccountVerification accountVerification);
    List<AccountVerification> findByUserid(Long userid);
    Optional<AccountVerification> findByUrl(String url);
}