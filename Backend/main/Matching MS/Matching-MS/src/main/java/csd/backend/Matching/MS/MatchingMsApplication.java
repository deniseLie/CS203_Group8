package csd.backend.Matching.MS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"csd.backend.Matching.MS", "csd.backend.config"})
public class MatchingMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchingMsApplication.class, args);
	}
}
