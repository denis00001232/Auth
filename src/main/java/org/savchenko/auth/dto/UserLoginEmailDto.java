package org.savchenko.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEmailDto {
    @NotBlank(message = "email обязателен")
    @Email(message = "введите корректный email")
    private String email;
    @NotBlank(message = "пароль обязателен")
    private String password;
}
