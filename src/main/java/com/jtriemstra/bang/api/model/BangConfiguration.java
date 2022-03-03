package com.jtriemstra.bang.api.model;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jtriemstra.bang.api.model.deck.RoleDeck;

@Configuration
public class BangConfiguration {

	@Bean
	Supplier<RoleDeck> defaultRoleDeckFactory() {
		return () -> new RoleDeck();
	}
}
