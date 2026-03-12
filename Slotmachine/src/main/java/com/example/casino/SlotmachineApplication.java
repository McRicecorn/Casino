package com.example.casino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootApplication
public class SlotmachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlotmachineApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Random random() {
		return new Random();
	}

}
