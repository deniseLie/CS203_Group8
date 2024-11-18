package csd.backend.Matching.MS.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import csd.backend.Matching.MS.service.matchmaking.MatchmakingQueueListener;

@Configuration
public class MatchmakingQueueListenerConfig {

    private final MatchmakingQueueListener matchmakingQueueListener;

    public MatchmakingQueueListenerConfig(MatchmakingQueueListener matchmakingQueueListener) {
        this.matchmakingQueueListener = matchmakingQueueListener;
    }

    @Bean
    CommandLineRunner startMatchingQueueListener() {
        return args -> new Thread(matchmakingQueueListener::listenToMatchingQueue).start();
    }
}
