package com.ornek.springbootproje.controller;

import com.ornek.springbootproje.entities.*;
import com.ornek.springbootproje.manager.LogManager;
import com.ornek.springbootproje.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final TwoFactorVerificationService twoFactorVerificationService;
    private final LogManager logManager;

    @Autowired
    public UserController(UserService userService, MailService mailService, TwoFactorVerificationService twoFactorVerificationService, LogManager logManager) {
        this.userService = userService;
        this.mailService = mailService;
        this.twoFactorVerificationService = twoFactorVerificationService;
        this.logManager = logManager;
    }

    // Kullanıcı ekleme
    @PostMapping("/adduser")
    public ResponseEntity<?> AddUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Validasyon hatalarını toplayıp kullanıcıya dönelim
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            logManager.logInfo("Yeni bir kullanıcı ekleniyor: " + user.getEmail());
            User createdUser = userService.AddUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logManager.logError("Kullanıcı eklenirken hata oluştu", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Kullanıcı güncelleme
    @PutMapping("/updateuser/{id}")
    public ResponseEntity<User> UpdateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.UpdateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Kullanıcı silme
    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<Void> DeleteUser(@PathVariable Long id) {
        try {
            userService.DeleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Kullanıcı bilgilerini ID ile alma
    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<User> GetUserById(@PathVariable Long id) {
        User user = userService.GetUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Tüm kullanıcıları alma
    @GetMapping("/getallusers")
    public ResponseEntity<List<User>> GetAllUsers() {
        List<User> users = userService.GetAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Kullanıcı hesabını doğrulama metodu
    @PostMapping("/verify")
    public String VerifyAccount(@RequestParam String verificationUrl) {
        try {
            boolean isVerified = userService.VerifyAccount(verificationUrl);
            if (isVerified) {
                return "Hesap başarıyla doğrulandı.";
            } else {
                return "Hesap doğrulama başarısız. Lütfen geçerli bir bağlantı kullanın.";
            }
        } catch (Exception e) {
            return "Hesap doğrulama sırasında bir hata oluştu: " + e.getMessage();
        }
    }

    // Şifre sıfırlama isteği
    @PostMapping("/request-password-reset")
    public String RequestPasswordReset(@RequestParam String email) throws Exception {
        return userService.RequestPasswordReset(email);
    }

    @GetMapping("/verify-password-reset")
    public ResponseEntity<String> VerifyPasswordReset(@RequestParam String token) {
        if (token != null && userService.IsTokenValid(token)) {
            return ResponseEntity.ok("Token geçerli, şifre sıfırlama sayfasına yönlendiriliyor.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token geçersiz veya süresi dolmuş.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam String token, @RequestParam String newPassword) throws Exception {
        if (token != null && userService.IsTokenValid(token)) {
            Long userId = userService.GetUserIdByToken(token); // Token'dan kullanıcı ID'sini al
            userService.ResetPassword(userId, newPassword);
            return ResponseEntity.ok("Şifre başarıyla sıfırlandı.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token geçersiz veya süresi dolmuş.");
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> SendVerificationCode(@RequestParam Long userId) {
        try {
            // Kullanıcı bilgisini al
            User user = userService.GetUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı.");
            }
            var verification = twoFactorVerificationService.SendVerificationCode(userId);
            String userPhoneNumber = user.getPhone(); // Kullanıcının telefon numarasını al
            mailService.SendEmail(user.getEmail(), "MFA Aktivasyon", verification.getCode()); // Kullanıcıya mail gönderimi
            return ResponseEntity.ok("Doğrulama kodu gönderildi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kod gönderiminde bir hata oluştu: " + e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> VerifyCode(@RequestParam Long userId, @RequestParam String code) {
        boolean isValid = twoFactorVerificationService.VerifyCode(userId, code);
        if (isValid) {
            return ResponseEntity.ok("Kod geçerli. İki faktörlü kimlik doğrulama başarılı.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kod veya süresi dolmuş.");
    }
}