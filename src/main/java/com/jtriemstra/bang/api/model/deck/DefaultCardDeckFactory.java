package com.jtriemstra.bang.api.model.deck;

import org.springframework.stereotype.Component;

@Component
public class DefaultCardDeckFactory implements CardDeckFactory {
	public CardDeck create() {
		return new RealCardDeck(); 
	}
}
