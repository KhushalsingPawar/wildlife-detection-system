package com.example.wildlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class WildlifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WildlifeApplication.class, args);
	}

}

	