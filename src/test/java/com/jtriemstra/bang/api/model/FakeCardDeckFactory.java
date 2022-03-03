package com.jtriemstra.bang.api.model;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.deck.CardDeck;
import com.jtriemstra.bang.api.model.deck.CardDeckFactory;

@Component
@Primary
@Profile("integration")
public class FakeCardDeckFactory implements CardDeckFactory {

	@Override
	public CardDeck create() {
		return new FakeCardDeck();
	}
	
}
