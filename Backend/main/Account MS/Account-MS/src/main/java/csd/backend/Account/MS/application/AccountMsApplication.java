package csd.backend.Account.MS.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"csd.backend.Account.MS"})
public class AccountMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountMsApplication.class, args);
	}

}