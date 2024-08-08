package com.example.ustudy_first_project.service;


import com.example.ustudy_first_project.entity.User;
import com.example.ustudy_first_project.repository.UserRepository;
import com.example.ustudy_first_project.service.register.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.registration.confirmation.code.length:4}")
    private int confirmationCodeLength;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User register(RegistrationRequest registrationRequest) {
        if (!registrationRequest.getPassword().equals(registrationRequest.getRepeatPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        if (userRepository.findByPhone(registrationRequest.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Телефон уже зарегистрирован");
        }

        User user = new User();
        user.setName(registrationRequest.getName());
        user.setPhone(registrationRequest.getPhone());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole());
        user.setActivated(false);

        User savedUser = userRepository.save(user);

        String code = generateConfirmationCode();
        emailService.sendConfirmationEmail(registrationRequest.getEmail(), code);

        savedUser.setConfirmationCode(code);
        userRepository.save(savedUser);

        return savedUser;
    }

    private String generateConfirmationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%0" + confirmationCodeLength + "d", random.nextInt((int) Math.pow(10, confirmationCodeLength)));
    }

    public boolean verifyConfirmationCode(Long userId, String code) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (code.equals(user.getConfirmationCode())) {
            user.setConfirmationCode(null);
            user.setActivated(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}

