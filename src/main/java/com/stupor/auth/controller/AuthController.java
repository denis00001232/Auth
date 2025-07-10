package com.stupor.auth.controller;

import com.stupor.auth.dto.controller.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.stupor.auth.kafka.KafkaProducer;
import com.stupor.auth.keycloak.KeyCloakTokenManager;
import com.stupor.auth.keycloak.KeycloakRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakRegisterService keycloakRegisterService;
    private final KeyCloakTokenManager keyCloakTokenManager;
    private final KafkaProducer kafkaProducer;

    /**
     * Регистрация работает следующим образом, у нас есть пользователь который в key cloak
     * должен быть верифицирован, если он верифицирован(через почту) мы его не можем удалить
     * в противном случае если на эндпоинт прилетает запрос о регистрации с таким-то email username
     * то не верифицированные пользователи удаляются у которых совпал email или username с новым запросом
     * @param userRegisterDto
     */
    @PostMapping("/register")
    public ResponseEntity<UserAnswerRegisterDto> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        userRegisterDto.setEmail(userRegisterDto.getUsername() + "@mail.ru");
        keycloakRegisterService.createUser(userRegisterDto);
        return ResponseEntity.status(201).body(new UserAnswerRegisterDto(userRegisterDto));
    }

    @PostMapping("/login")
    @Deprecated
    public TokenBoxDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        TokenBoxDto tokenBoxDto = keyCloakTokenManager.login(userLoginDto.getUsername().toLowerCase(),
                userLoginDto.getPassword());
        kafkaProducer.sendLogin(userLoginDto);
        return tokenBoxDto;
    }

    @PostMapping("/login-od")
    @Deprecated
    public TokenBoxDto loginOd(@RequestBody @Valid UserLoginOdDto userLoginOdDto) {
        TokenBoxDto tokenBoxDto = keyCloakTokenManager.login(userLoginOdDto.getUsername().toLowerCase(),
                userLoginOdDto.getPassword());
        return tokenBoxDto;
    }

    @PostMapping("/refresh")
    public TokenBoxDto refresh(@RequestBody TokenBoxDto tokenBoxDto) {
        return keyCloakTokenManager.refresh(tokenBoxDto.getRefreshToken());
    }

    @PostMapping("/logout")
    @Deprecated
    public void logout(@RequestHeader String username) {
        kafkaProducer.sendLogout(username);
    }

    @PostMapping("/recover")
    public MessageDto recover(@RequestBody String email) {
        keycloakRegisterService.recoverPassword(email);
        return new MessageDto("Успешно");
    }

    @GetMapping("/test")
    public MessageDto test(@RequestHeader String username) {
        return new MessageDto(username + " был успешно протестирован");
    }
}

