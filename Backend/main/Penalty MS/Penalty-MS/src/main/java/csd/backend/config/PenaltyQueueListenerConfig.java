package csd.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import csd.backend.Penalty.MS.PenaltyQueueListener;

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
