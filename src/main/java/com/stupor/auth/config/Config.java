package com.stupor.auth.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import com.stupor.auth.service.PathManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public Keycloak keycloak(PathManager pathManager) {
        return KeycloakBuilder.builder()
                .serverUrl(pathManager.getKeyCloakAddress(""))
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("186Kdc9899K")
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
