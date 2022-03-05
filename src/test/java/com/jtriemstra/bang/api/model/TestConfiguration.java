package com.jtriemstra.bang.api.model;

import java.util.UUID;
import java.util.function.Supplier;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.jtriemstra.bang.api.model.deck.RoleDeck;

@Configuration
public class TestConfiguration {

	@Bean
	@Primary
	@Profile("integration")
	Supplier<RoleDeck> mockRoleDeckFactory() {
		return () -> {
			RoleDeck result = Mockito.spy(new RoleDeck());
			Mockito.doReturn(Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW).when(result).draw();
			return result;
		};
	}

	@Bean
	@Primary
	@Profile("integration")
	public Supplier<UUID> fakeIDGenerator() {
		return () -> UUID.nameUUIDFromBytes("Test Game 1".getBytes());
	}
}
