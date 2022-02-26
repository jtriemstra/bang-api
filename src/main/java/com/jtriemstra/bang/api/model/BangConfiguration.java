package com.jtriemstra.bang.api.model;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BangConfiguration {

	@Bean
	Supplier<RoleDeck> defaultRoleDeckFactory() {
		return () -> new RoleDeck();
	}
}
