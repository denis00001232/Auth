package org.savchenko.auth.keycloak;

import lombok.RequiredArgsConstructor;
import org.savchenko.auth.dto.TokenBoxDto;
import org.savchenko.auth.exception.LogInException;
import org.savchenko.auth.exception.RefreshException;
import org.savchenko.auth.service.PathManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeyCloakTokenManager {
    private final PathManager pathManager;
    private final String keycloakTokenUrl = "/realms/axolotl/protocol/openid-connect/token";
    private final String clientId = "client";
    private final String clientSecret = "1I9ExWWxeOFd9vFUlUMYSRXX8WDpPo73";
    private final RestTemplate restTemplate;

    public TokenBoxDto login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<TokenBoxDto> response = restTemplate.postForEntity(
                    pathManager.getKeyCloakAddress(keycloakTokenUrl),
                    request,
                    TokenBoxDto.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new LogInException();
        }
    }

    public TokenBoxDto loginEmail(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("email", email);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<TokenBoxDto> response = restTemplate.postForEntity(
                    pathManager.getKeyCloakAddress(keycloakTokenUrl),
                    request,
                    TokenBoxDto.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new LogInException();
        }
    }

    public TokenBoxDto refresh(String refreshToken) {
        System.out.println(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TokenBoxDto> response = restTemplate.postForEntity(
                    pathManager.getKeyCloakAddress(keycloakTokenUrl),
                    request,
                    TokenBoxDto.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RefreshException();
        }
    }
}

