package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.TwoFactorVerification;
import com.ornek.springbootproje.repository.TwoFactorVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TwoFactorVerificationService {
    private final TwoFactorVerificationRepository verificationRepository;

    @Autowired
    public TwoFactorVerificationService(TwoFactorVerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public TwoFactorVerification SendVerificationCode(Long userId) throws Exception {
        // Rastgele bir doğrulama kodu oluştur
        String verificationCode = String.valueOf(new Random().nextInt(999999)); // 6 haneli kod
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(5); // 5 dakika geçerli

        // Doğrulama kaydını oluştur ve kaydet
        TwoFactorVerification verification = new TwoFactorVerification();
        verification.setUserId(userId);
        verification.setCode(verificationCode);
        verification.setExpirationDate(expirationDate);

        return verificationRepository.save(verification);
    }

    public boolean VerifyCode(Long userId, String code) {
        TwoFactorVerification verification = verificationRepository.findByUserIdAndCode(userId, code);

        return verification != null && verification.getExpirationDate().isAfter(LocalDateTime.now());
    }
}
