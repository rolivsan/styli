package br.com.styli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StyliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StyliApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(
		) {

		return args -> {


			System.out.println("subindo");
		};
	}
}
