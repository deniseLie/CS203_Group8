package csd.backend.config;

import csd.backend.Matching.MS.MatchmakingQueueListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
