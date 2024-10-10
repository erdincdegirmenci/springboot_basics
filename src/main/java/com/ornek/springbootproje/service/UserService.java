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

    public User AddUser(User user) throws Exception {

        try {
            if (IsEmailExists(user.getEmail())) {
                throw new Exception("Bu e-posta adresi zaten kayıtlı.");
            }

            // Kullanıcıyı oluştur
            user.setIsactive(false); // Kullanıcı aktif değil
            user.setIslock(true);    // Kullanıcı kilitli

            // Kullanıcıyı kaydet
            User savedUser = userRepository.save(user);

            // Varsayılan rolü ekle
            UserRole userRole = new UserRole();
            userRole.setUserid(savedUser.getId()); // Kaydedilen kullanıcının ID'sini al
            userRole.setRoleid(RoleTypes.ROLE_USER.getRoleId()); // Varsayılan rolü al

            // UserRole tablosuna kaydet
            userRoleRepository.save(userRole);

            // Hesap doğrulama kaydı oluştur
            AccountVerification accountVerification = new AccountVerification();
            accountVerification.setUserid(savedUser.getId());
            String verificationUrl = GenerateVerificationUrl(savedUser); // Doğrulama URL'sini oluştur
            accountVerification.setUrl(verificationUrl);

            // AccountVerification tablosuna kaydet
            accountVerificationRepository.save(accountVerification);

            // Kullanıcıya e-posta gönder
            SendVerificationEmail(savedUser.getEmail(), verificationUrl); // E-posta gönderme metodu

            return savedUser;

        } catch (Exception e) {
            throw new Exception("Kullanıcı eklenirken bir hata oluştu: " + e.getMessage());
        }
    }


    public User UpdateUser(User user) {
        return userRepository.save(user);
    }


    public void DeleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User GetUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public List<User> GetAllUsers() {
        return userRepository.findAll();
    }


    public boolean IsEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Hesap doğrulama URL'sini oluşturmak için yardımcı metod
    private String GenerateVerificationUrl(User user) {
        return "http://erdincdegirmenci.com/verify?userId=" + user.getId();
    }

    // Kullanıcıya doğrulama e-postası göndermek için metot
    private void SendVerificationEmail(String email, String verificationUrl) throws Exception {
        String subject = "Hesap Doğrulama";
        String body = "Hesabınızı doğrulamak için lütfen aşağıdaki linke tıklayın: " + verificationUrl;
        mailService.SendEmail(email, subject, body);
    }

    public boolean VerifyAccount(String verificationUrl) throws Exception {
        try {
            Optional<AccountVerification> accountVerificationOpt = accountVerificationRepository.findByUrl(verificationUrl);
            if (accountVerificationOpt.isPresent()) {
                AccountVerification accountVerification = accountVerificationOpt.get();
                Optional<User> userOpt = userRepository.findById(accountVerification.getUserid());

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

    public String RequestPasswordReset(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new Exception("Kullanıcı bulunamadı.");
        }

        // ResetPasswordVerification kaydını oluştur
        ResetPasswordVerification resetPasswordVerification = new ResetPasswordVerification();
        resetPasswordVerification.setUserId(user.get().getId());
        resetPasswordVerification.setUrl(GenerateResetPasswordUrl(user.get())); // URL oluştur
        resetPasswordVerification.setExpirationDate(LocalDateTime.now().plusHours(24)); // 24 saat sonra süresi dolacak resetPasswordVerificationRepository.save(resetPasswordVerification);

        // Kullanıcıya e-posta gönder
        SendResetPasswordEmail(email, resetPasswordVerification.getUrl());

        return "Şifre sıfırlama isteği başarıyla gönderildi.";
    }

    public String GenerateResetPasswordUrl(User user) {
        String token = jwtUtil.generateToken(user);
        return "https://yourapp.com/reset-password?token=" + token;
    }

    private void SendResetPasswordEmail(String email, String resetPasswordUrl) throws Exception {
        String subject = "Şifre Sıfırlama";
        String body = "Şifrenizi sıfırlamak için lütfen aşağıdaki linke tıklayın: " + resetPasswordUrl;
        mailService.SendEmail(email, subject, body);
    }

    public void ResetPassword(Long userId, String newPassword) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Kullanıcı bulunamadı."));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean VerifyResetPasswordToken(String url) {
        // URL ile doğrulama kaydını kontrol et
        ResetPasswordVerification verification = resetPasswordVerificationRepository.findByUrl(url);
        if (verification != null) {
            // Geçerlilik süresini kontrol et
            return verification.getExpirationDate().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public boolean IsTokenExpired(Date expirationDate) {
        return expirationDate.before(new Date()); // Şu anki tarih, expirationDate'den önceyse token süresi dolmuş
    }

    public boolean IsTokenValid(String token) {
        ResetPasswordVerification verification = resetPasswordVerificationRepository.findByUrl(token);
        if (verification == null) {
            return false;
        }
        return !IsTokenExpired(Date.from(verification.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant())); // Token geçerli mi?
    }

    public Long GetUserIdByToken(String token) {
        String userEmail = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(userEmail).get().getId();
    }
}
