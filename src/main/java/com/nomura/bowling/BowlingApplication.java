package com.nomura.bowling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.nomura.bowling" })
public class BowlingApplication {
	public static void main(String[] args) {
		SpringApplication.run(BowlingApplication.class, args);
	}
}


