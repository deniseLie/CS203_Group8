package csd.backend.Penalty.MS.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = {"csd.backend.Penalty.MS"})
public class PenaltyMsApplication {

    public static void main(String[] args) {
        
        // Dotenv
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("AWS_REGION", dotenv.get("AWS_REGION"));
		System.setProperty("ACCOUNT_QUEUE_URL", dotenv.get("ACCOUNT_QUEUE_URL"));
		System.setProperty("MATCHMAKING_QUEUE_URL", dotenv.get("MATCHMAKING_QUEUE_URL"));
		System.setProperty("PENALTY_QUEUE_URL", dotenv.get("PENALTY_QUEUE_URL"));
		System.setProperty("LOGIN_QUEUE_URL", dotenv.get("LOGIN_QUEUE_URL"));
		System.setProperty("ADMIN_QUEUE_URL", dotenv.get("ADMIN_QUEUE_URL"));
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		
        SpringApplication.run(PenaltyMsApplication.class, args);
    }
}