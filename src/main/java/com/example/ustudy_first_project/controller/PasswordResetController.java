package com.example.ustudy_first_project.controller;

import com.example.ustudy_first_project.entity.User;
import com.example.ustudy_first_project.repository.UserRepository;
import com.example.ustudy_first_project.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return ResponseEntity.ok("Письмо с инструкциями по восстановлению пароля отправлено на ваш email.");
    }

    @PostMapping("/reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestParam String token, @RequestParam String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Неверный токен"));

        if (LocalDateTime.now().isAfter(user.getResetTokenExpiration())) {
            return ResponseEntity.badRequest().body("Срок действия токена истек.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);

        return ResponseEntity.ok("Пароль успешно обновлен.");
    }
}

