package com.example.ustudy_first_project.service.register;

import com.example.ustudy_first_project.roles.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @NotEmpty(message = "Обязательное поле")
    private String name;

    @NotEmpty(message = "Обязательное поле")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неправильный формат номера телефона")
    private String phone;

    @NotEmpty(message = "Обязательное поле")
    @Email(message = "Неправильный формат")
    private String email;

    @NotEmpty(message = "Обязательное поле")
    @Size(min = 6, message = "Минимум 6 символов")
    private String password;

    @NotEmpty(message = "Обязательное поле")
    private String repeatPassword;

    @NotNull(message = "Обязательное поле")
    private Role role;
}
