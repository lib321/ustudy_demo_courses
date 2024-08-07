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

    @Value("${app.registration.confirmation.code.expiration:15}")
    private long confirmationCodeExpirationMinutes;

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

        User savedUser = userRepository.save(user);

        String code = generateConfirmationCode();
        emailService.sendConfirmationEmail(registrationRequest.getEmail(), code);

        savedUser.setConfirmationCode(code);
        savedUser.setConfirmationCodeExpiration(LocalDateTime.now().plusMinutes(confirmationCodeExpirationMinutes));
        userRepository.save(savedUser);

        return savedUser;
    }

    private String generateConfirmationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%0" + confirmationCodeLength + "d", random.nextInt((int) Math.pow(10, confirmationCodeLength)));
    }

    public boolean verifyConfirmationCode(Long userId, String code) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (code.equals(user.getConfirmationCode()) && LocalDateTime.now().isBefore(user.getConfirmationCodeExpiration())) {
            user.setConfirmationCode(null);
            user.setConfirmationCodeExpiration(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

//    public User authenticate(String phone, String password) {
//        Optional<User> userOpt = userRepository.findByPhone(phone);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            if (passwordEncoder.matches(password, user.getPassword())) {
//                return user;
//            }
//        }
//        throw new IllegalArgumentException("Неверный номер или пароль");
//    }
}
