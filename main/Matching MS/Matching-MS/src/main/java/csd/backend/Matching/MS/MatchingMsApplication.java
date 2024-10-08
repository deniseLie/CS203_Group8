package csd.backend.Matching.MS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import csd.backend.Matching.MS.Player.Player;
import csd.backend.Matching.MS.Player.PlayerRepository;

@SpringBootApplication
public class MatchingMsApplication {

	private static final Logger log = LoggerFactory.getLogger(MatchingMsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MatchingMsApplication.class, args);
	}

	@Bean
  	public CommandLineRunner demo(PlayerRepository repository) {
		return (args) -> {

		// fetch all players
		log.info("Players found with findAll():");
		log.info("-------------------------------");
		repository.findAll().forEach(player -> {
			log.info(player.toString());
		});
		log.info("");

		// fetch an individual player by ID
		Player player = repository.findById(1);
		log.info("Player found with findById(1):");
		log.info("--------------------------------");
		log.info(player.toString());
		log.info("");
		};
	}

}
