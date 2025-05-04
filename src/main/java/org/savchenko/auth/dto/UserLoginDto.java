package org.savchenko.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "username обязателен")
    private String username;
    @NotBlank(message = "пароль обязателен")
    private String password;
}
