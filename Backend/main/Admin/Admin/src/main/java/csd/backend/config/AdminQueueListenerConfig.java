package csd.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import csd.backend.Admin.AdminQueueListener;

@Configuration
public class AdminQueueListenerConfig {

    private final AdminQueueListener adminQueueListener;

    public AdminQueueListenerConfig(AdminQueueListener adminQueueListener) {
        this.adminQueueListener = adminQueueListener;
    }

    @Bean
    CommandLineRunner startAdminQueueListener() {
        return args -> new Thread(adminQueueListener::listenToAdminQueue).start();
    }
}