package org.savchenko.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication

public class AuthApplication {



    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
