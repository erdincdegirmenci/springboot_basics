package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.User;
import com.ornek.springbootproje.repository.UserRepository;
import com.ornek.springbootproje.security.JwtTokenUtil;
import com.ornek.springbootproje.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || user.get().getIslock()) {
            throw new BadCredentialsException("Kullanıcı kilitli veya mevcut değil.");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            user.get().setFailedLoginAttemp(user.get().getFailedLoginAttemp() + 1);
            if (user.get().getFailedLoginAttemp() >= 3) {
                user.get().setIslock(true);
                userRepository.save(user.get()); // Kullanıcıyı güncelle
                throw new BadCredentialsException("Kullanıcı kilitli.");
            }
            userRepository.save(user.get()); // Giriş denemesi güncelle
            throw new BadCredentialsException("Geçersiz şifre.");
        }

        // Başarılı giriş sonrası giriş denemelerini sıfırla
        user.get().setFailedLoginAttemp(0);
        userRepository.save(user.get());

        return jwtUtil.generateToken(user.get());
    }
}