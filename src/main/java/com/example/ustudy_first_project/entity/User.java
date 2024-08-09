package com.example.ustudy_first_project.entity;

import com.example.ustudy_first_project.roles.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Обязательное поле")
    private String name;

    @NotEmpty(message = "Обязательное поле")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат")
    private String phone;

    @NotEmpty(message = "Обязательное поле")
    @Email(message = "Неправильный формат")
    private String email;

    @NotEmpty(message = "Обязательное поле")
    @Size(min = 6, message = "минимум 6 символов")
    private String password;

    @NotNull(message = "Обязательное поле")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String confirmationCode;

    private boolean isActivated = false;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> favoriteCourses = new HashSet<>();
}
