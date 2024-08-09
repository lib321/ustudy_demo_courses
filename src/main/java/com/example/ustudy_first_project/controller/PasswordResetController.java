package com.example.ustudy_first_project.controller;

import com.example.ustudy_first_project.entity.User;
import com.example.ustudy_first_project.repository.UserRepository;
import com.example.ustudy_first_project.service.EmailService;
import com.example.ustudy_first_project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            userService.requestPasswordReset(email);
            return ResponseEntity.ok("Код восстановления пароля отправлен на вашу почту.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestParam Long userId, @RequestParam String code, @RequestParam String newPassword) {
        boolean isVerified = userService.verifyPasswordResetCode(userId, code);

        if (isVerified) {
            userService.resetPassword(userId, newPassword);
            return ResponseEntity.ok("Пароль успешно изменен.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Неверный или истекший код восстановления пароля.");
        }
    }
}

