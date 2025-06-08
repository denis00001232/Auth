package org.savchenko.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.savchenko.auth.dto.*;

import org.savchenko.auth.keycloak.KeyCloakTokenManager;
import org.savchenko.auth.keycloak.KeycloakRegisterService;
import org.savchenko.auth.model.User;
import org.savchenko.auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final KeycloakRegisterService keycloakRegisterService;
    private final KeyCloakTokenManager keyCloakTokenManager;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Регистрация работает следующим образом, у нас есть пользователь который в key cloak
     * должен быть верифицирован, если он верифицирован(через почту) мы его не можем удалить
     * в противном случае если на эндпоинт прилетает запрос о регистрации с таким-то email username
     * то не верифицированные пользователи удаляются у которых совпал email или username с новым запросом
     * @param userRegisterDto
     */
    @PostMapping("/register")
    public ResponseEntity<Message> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
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
        kafkaTemplate.send("person-register", userRegisterDto.getUsername());
        return ResponseEntity.ok(new Message("Успех"));
    }

    //@PostMapping("/login")
    @Deprecated
    public TokenBoxDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        kafkaTemplate.send("person-login", userLoginDto.getUsername());
        return keyCloakTokenManager.login(userLoginDto.getUsername().toLowerCase(), userLoginDto.getPassword());
    }

    @PostMapping("/login_email")
    public TokenBoxDto loginEmail(@RequestBody @Valid UserLoginEmailDto userLoginEmailDto) {
        String username = userRepository.findByEmail(userLoginEmailDto.getEmail()).get().getUsername();
        kafkaTemplate.send("person-login", username);
        return keyCloakTokenManager.login(userLoginEmailDto.getEmail().toLowerCase(), userLoginEmailDto.getPassword());
    }

    @PostMapping("/refresh")
    public TokenBoxDto refresh(@RequestBody TokenBoxDto tokenBoxDto) {
        return keyCloakTokenManager.refresh(tokenBoxDto.getRefreshToken());
    }

    @PostMapping("/recover")
    public Message recover(@RequestBody String email) {
        keycloakRegisterService.recoverPassword(email);
        return new Message("Успешно");
    }

    @GetMapping("/test")
    public Message test(@RequestHeader String username, @RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        return new Message(username + " был успешно протестирован");
    }
}

