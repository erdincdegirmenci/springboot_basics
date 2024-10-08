package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.*;
import com.ornek.springbootproje.enums.RoleTypes;
import com.ornek.springbootproje.repository.*;
import com.ornek.springbootproje.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AccountVerificationRepository accountVerificationRepository;
    private final ResetPasswordVerificationRepository resetPasswordVerificationRepository;
    private final MailService mailService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, AccountVerificationRepository accountVerificationRepository, MailService mailService, ResetPasswordVerificationRepository resetPasswordVerificationRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.accountVerificationRepository = accountVerificationRepository;
        this.resetPasswordVerificationRepository = resetPasswordVerificationRepository;
        this.mailService = mailService;
    }

    public User addUser(User user) throws Exception {

        try {
            if (isEmailExists(user.getEmail())) {
                throw new Exception("Bu e-posta adresi zaten kayıtlı.");
            }

            // Kullanıcıyı oluştur
            user.setIsactive(false); // Kullanıcı aktif değil
            user.setIslock(true);    // Kullanıcı kilitli

            // Kullanıcıyı kaydet
            User savedUser = userRepository.save(user);

            // Varsayılan rolü ekle
            UserRole userRole = new UserRole();
            userRole.setUserId(savedUser.getId()); // Kaydedilen kullanıcının ID'sini al
            userRole.setRoleId(RoleTypes.ROLE_USER.getRoleId()); // Varsayılan rolü al

            // UserRole tablosuna kaydet
            userRoleRepository.save(userRole);

            // Hesap doğrulama kaydı oluştur
            AccountVerification accountVerification = new AccountVerification();
            accountVerification.setUserId(savedUser.getId());
            String verificationUrl = generateVerificationUrl(savedUser); // Doğrulama URL'sini oluştur
            accountVerification.setUrl(verificationUrl);

            // AccountVerification tablosuna kaydet
            accountVerificationRepository.save(accountVerification);

            // Kullanıcıya e-posta gönder
            sendVerificationEmail(savedUser.getEmail(), verificationUrl); // E-posta gönderme metodu

            return savedUser;

        } catch (Exception e) {
            throw new Exception("Kullanıcı eklenirken bir hata oluştu: " + e.getMessage());
        }
    }


    public User updateUser(User user) {
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Hesap doğrulama URL'sini oluşturmak için yardımcı metod
    private String generateVerificationUrl(User user) {
        return "http://example.com/verify?userId=" + user.getId();
    }

    // Kullanıcıya doğrulama e-postası göndermek için metot
    private void sendVerificationEmail(String email, String verificationUrl) {
        String subject = "Hesap Doğrulama";
        String body = "Hesabınızı doğrulamak için lütfen aşağıdaki linke tıklayın: " + verificationUrl;
        mailService.sendEmail(email, subject, body);
    }

    public boolean verifyAccount(String verificationUrl) throws Exception {
        try {
            Optional<AccountVerification> accountVerificationOpt = accountVerificationRepository.findByUrl(verificationUrl);
            if (accountVerificationOpt.isPresent()) {
                AccountVerification accountVerification = accountVerificationOpt.get();
                Optional<User> userOpt = userRepository.findById(accountVerification.getUserId());

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setIsactive(true);
                    user.setIslock(false);

                    userRepository.save(user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Hesap doğrulama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    public String requestPasswordReset(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new Exception("Kullanıcı bulunamadı.");
        }

        // ResetPasswordVerification kaydını oluştur
        ResetPasswordVerification resetPasswordVerification = new ResetPasswordVerification();
        resetPasswordVerification.setUserId(user.get().getId());
        resetPasswordVerification.setUrl(generateResetPasswordUrl(user.get())); // URL oluştur
        resetPasswordVerification.setExpirationDate(LocalDateTime.now().plusHours(24)); // 24 saat sonra süresi dolacak resetPasswordVerificationRepository.save(resetPasswordVerification);

        // Kullanıcıya e-posta gönder
        sendResetPasswordEmail(email, resetPasswordVerification.getUrl());

        return "Şifre sıfırlama isteği başarıyla gönderildi.";
    }

    public String generateResetPasswordUrl(User user) {
        String token = jwtUtil.generateToken(user);
        return "https://yourapp.com/reset-password?token=" + token;
    }

    private void sendResetPasswordEmail(String email, String resetPasswordUrl) {
        String subject = "Şifre Sıfırlama";
        String body = "Şifrenizi sıfırlamak için lütfen aşağıdaki linke tıklayın: " + resetPasswordUrl;
        mailService.sendEmail(email, subject, body);
    }

    public void resetPassword(Long userId, String newPassword) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Kullanıcı bulunamadı."));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean verifyResetPasswordToken(String url) {
        // URL ile doğrulama kaydını kontrol et
        ResetPasswordVerification verification = resetPasswordVerificationRepository.findByUrl(url);
        if (verification != null) {
            // Geçerlilik süresini kontrol et
            return verification.getExpirationDate().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public boolean isTokenExpired(Date expirationDate) {
        return expirationDate.before(new Date()); // Şu anki tarih, expirationDate'den önceyse token süresi dolmuş
    }

    public boolean isTokenValid(String token) {
        ResetPasswordVerification verification = resetPasswordVerificationRepository.findByUrl(token);
        if (verification == null) {
            return false;
        }
        return !isTokenExpired(Date.from(verification.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant())); // Token geçerli mi?
    }

    public Long getUserIdByToken(String token) {
        String userEmail = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(userEmail).get().getId();
    }
}
