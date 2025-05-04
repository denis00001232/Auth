package org.savchenko.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @Size(min = 2, max = 20, message = "Имя должно быть от 2 до 20 символов")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 20, message = "Фамилия должна быть от 2 до 20 символов")
    private String surname;

    @NotBlank(message = "username обязателен")
    @Size(min = 4, max = 20, message = "username должнен быть от 4 до 20 символов")
    private String username;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
}
