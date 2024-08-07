package com.example.ustudy_first_project.service;


import com.example.ustudy_first_project.entity.User;
import com.example.ustudy_first_project.repository.UserRepository;
import com.example.ustudy_first_project.service.register.RegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        return userRepository.save(user);
    }

    public User authenticate(String phone, String password) {
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new IllegalArgumentException("Неверный номер или пароль");
    }
}
