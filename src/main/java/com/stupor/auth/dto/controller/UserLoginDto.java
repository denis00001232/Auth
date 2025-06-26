package com.stupor.auth.dto.controller;

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

    @NotBlank(message = "callSign обязателен")
    private String callSign = "Горыныч";

    @NotBlank(message = "пароль обязателен")
    private String password;
}
