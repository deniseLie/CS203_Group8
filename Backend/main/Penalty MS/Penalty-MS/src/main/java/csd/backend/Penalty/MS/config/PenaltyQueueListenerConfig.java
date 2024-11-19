package csd.backend.Penalty.MS.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import csd.backend.Penalty.MS.controller.PenaltyQueueListener;

@Configuration
public class PenaltyQueueListenerConfig {

    private final PenaltyQueueListener penaltyQueueListener;

    public PenaltyQueueListenerConfig(PenaltyQueueListener penaltyQueueListener) {
        this.penaltyQueueListener = penaltyQueueListener;
    }

    @Bean
    CommandLineRunner startPenaltyQueueListener() {
        return args -> new Thread(penaltyQueueListener::listenToPenaltyQueue).start();
    }
}
