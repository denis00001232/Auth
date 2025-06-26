package com.stupor.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PathManager {
    @Value("${KEYCLOAK_HOST:localhost}")
    private String keyCloakAddress;
    @Value("${DB_HOST:localhost}")
    private String postgresAddress;
    @Value("${SECURED_HTTP:http}")
    private String pathPrefix;

    public String getKeyCloakAddress(String URL) {
        return pathPrefix + "://" + keyCloakAddress + ":8080" + URL;
    }
}
