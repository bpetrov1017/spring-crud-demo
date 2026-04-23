package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostConstruct
	public void check() {
		System.out.println(">>> DEMO APPLICATION LOADED <<<");
	}

	@PostConstruct
	public void init() {
		System.out.println(">>> DEMO CONTROLLER CREATED <<<");
	}

}
