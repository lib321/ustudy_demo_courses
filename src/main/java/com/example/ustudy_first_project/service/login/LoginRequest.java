package com.example.ustudy_first_project.service.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {


    @NotEmpty(message = "Обязательное поле")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неправильный формат номера телефона")
    private String phone;

    @NotEmpty(message = "Обязательное поле")
    @Size(min = 6, message = "Минимум 6 символов")
    private String password;
}
