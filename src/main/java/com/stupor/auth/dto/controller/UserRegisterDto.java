package com.stupor.auth.dto.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotBlank(message = "username обязателен")
    @Size(min = 4, max = 20, message = "username должнен быть от 4 до 20 символов")
    @Pattern(regexp = "^[a-z_0-9]+$", message = "username может содержать только строчные латинские буквы и нижние подчёркивания")
    private String username;

    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
}
