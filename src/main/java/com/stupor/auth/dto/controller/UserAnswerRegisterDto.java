package com.stupor.auth.dto.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAnswerRegisterDto {
    private String username;
    private String password;

    public UserAnswerRegisterDto(UserRegisterDto userRegisterDto) {
        username = userRegisterDto.getUsername();
        password = userRegisterDto.getPassword();
    }
}
