package csd.backend.Penalty.MS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"csd.backend.Penalty.MS", "csd.backend.config"})
public class PenaltyMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PenaltyMsApplication.class, args);
    }
}