package com.jtriemstra.bang.api.model;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Primary
@Profile("integration")
public class FakeCardDeckFactory implements CardDeckFactory {

	@Override
	public CardDeck create() {
		return new FakeCardDeck();
	}
	
}
