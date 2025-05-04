package org.savchenko.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.savchenko.auth.dto.TokenBoxDto;
import org.savchenko.auth.dto.UserLoginDto;
import org.savchenko.auth.dto.UserRegisterDto;
import org.savchenko.auth.exception.EmailAlreadyExistsException;
import org.savchenko.auth.exception.UsernameAlreadyExistsException;
import org.savchenko.auth.keycloak.KeyCloakTokenManager;
import org.savchenko.auth.keycloak.KeycloakRegisterService;
import org.savchenko.auth.model.User;
import org.savchenko.auth.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final KeycloakRegisterService keycloakRegisterService;
    private final KeyCloakTokenManager keyCloakTokenManager;

    /**
     * Регистрация работает следующим образом, у нас есть пользователь который в key cloak
     * должен быть верифицирован, если он верифицирован(через почту) мы его не можем удалить
     * в противном случае если на эндпоинт прилетает запрос о регистрации с таким-то email username
     * то не верифицированные пользователи удаляются у которых совпал email или username с новым запросом
     * @param userRegisterDto
     */
    @PostMapping("/register")
    public String register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        userRegisterDto.setUsername(userRegisterDto.getUsername().toLowerCase());
        userRegisterDto.setEmail(userRegisterDto.getEmail().toLowerCase());

        Optional<User> userWithSuchUsername = userRepository.findByUsername(userRegisterDto.getUsername());
        if (userWithSuchUsername.isPresent()) {
            keycloakRegisterService.checkByUsername(userRegisterDto.getUsername());
            userRepository.delete(userWithSuchUsername.get());
        }
        Optional<User> userWithSuchEmail = userRepository.findByEmail(userRegisterDto.getEmail());
        if (userWithSuchEmail.isPresent()) {
            keycloakRegisterService.checkByEmail(userRegisterDto.getEmail());
            userRepository.delete(userWithSuchEmail.get());
        }
        keycloakRegisterService.createUser(userRegisterDto);
        userRepository.save(new User(userRegisterDto));
        return "успешно";
    }

    @PostMapping("/login")
    public TokenBoxDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return keyCloakTokenManager.login(userLoginDto.getUsername().toLowerCase(), userLoginDto.getPassword());
    }

    @PostMapping("/refresh")
    public TokenBoxDto refresh(@RequestBody String refreshToken) {
        return keyCloakTokenManager.refresh(refreshToken);
    }

    @PostMapping("/recover")
    public String recover(@RequestBody String email) {
        keycloakRegisterService.recoverPassword(email);
        return "Успешно";
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaimAsString("preferred_username") + " был успешно протестирован";
    }
}

