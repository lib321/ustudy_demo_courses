package com.example.ustudy_first_project.service;


import com.example.ustudy_first_project.entity.Course;
import com.example.ustudy_first_project.entity.User;
import com.example.ustudy_first_project.repository.CourseRepository;
import com.example.ustudy_first_project.repository.UserRepository;
import com.example.ustudy_first_project.service.register.RegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CourseRepository courseRepository;

    private static final int CONFIRMATION_CODE_LENGTH = 6;
    private static final int CONFIRMATION_CODE_EXPIRATION_HOURS = 1;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.courseRepository = courseRepository;
    }

    public void register(RegistrationRequest registrationRequest) {
        if (!registrationRequest.getPassword().equals(registrationRequest.getRepeatPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        if (userRepository.findByPhone(registrationRequest.getPhone()).isPresent() ||
                userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Телефон или почта уже зарегистрирован");
        }

        User user = new User();
        user.setName(registrationRequest.getName());
        user.setPhone(registrationRequest.getPhone());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole());
        user.setActivated(false);


        String code = generateConfirmationCode();
        user.setConfirmationCode(code);
        user.setConfirmationCodeExpiration(LocalDateTime.now().plusHours(CONFIRMATION_CODE_EXPIRATION_HOURS));

        User savedUser = userRepository.save(user);
        emailService.sendConfirmationEmail(registrationRequest.getEmail(), code);

        userRepository.save(savedUser);
    }

    private String generateConfirmationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%0" + CONFIRMATION_CODE_LENGTH + "d", random.nextInt((int) Math.pow(10, CONFIRMATION_CODE_LENGTH)));
    }

    public boolean verifyConfirmationCode(Long userId, String code) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (code.equals(user.getConfirmationCode())
                && user.getConfirmationCodeExpiration() != null
                && user.getConfirmationCodeExpiration().isAfter(LocalDateTime.now())) {

            user.setConfirmationCode(null);
            user.setConfirmationCodeExpiration(null);
            user.setActivated(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean verifyPasswordResetCode(Long userId, String code) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (user.getResetToken() == null || !user.getResetToken().equals(code)) {
            return false;
        }

        if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            user.setResetToken(null);
            user.setResetTokenExpiration(null);
            userRepository.save(user);
            return false;
        }

        return true;
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        String code = generateConfirmationCode();
        user.setResetToken(code);
        user.setResetTokenExpiration(LocalDateTime.now().plusHours(CONFIRMATION_CODE_EXPIRATION_HOURS));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, code);
    }

    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);
    }

    public void addToFavorites(long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        user.getFavoriteCourses().add(course);
        userRepository.save(user);
    }

    public void removeFromFavorites(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));

        user.getFavoriteCourses().remove(course);
        userRepository.save(user);
    }

    public Set<Course> getFavoriteCourses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return user.getFavoriteCourses();
    }
}

