package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

// import csd.backend.Matching.MS.Player.Player;
// import csd.backend.Matching.MS.Player.PlayerRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"csd.backend.config", "csd.backend.Matching.MS"})
public class MatchingMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchingMsApplication.class, args);
	}
}
