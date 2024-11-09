package com.loltournament.loginservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.loltournament.loginservice.LoginListener;

@Configuration
public class LoginListenerConfig {

    private final LoginListener loginListener;

    public LoginListenerConfig(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Bean
    CommandLineRunner startLoginListener() {
        return args -> new Thread(loginListener::listenToLogin).start();
    }
}
