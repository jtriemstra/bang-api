package com.jtriemstra.bang.api;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BangApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BangApiApplication.class, args);
	}

	@Bean
	public Supplier<UUID> defaultIDGenerator() {
		return () -> UUID.randomUUID();
	}
}
