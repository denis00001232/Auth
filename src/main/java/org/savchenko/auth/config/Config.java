package org.savchenko.auth.config;

import lombok.Value;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.savchenko.auth.service.PathManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/test").authenticated()
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("v3/api-docs").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));
//        return http.build();
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
