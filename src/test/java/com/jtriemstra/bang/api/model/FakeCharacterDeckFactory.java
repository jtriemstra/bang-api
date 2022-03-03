package com.jtriemstra.bang.api.model;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.deck.CharacterDeck;
import com.jtriemstra.bang.api.model.deck.CharacterDeckFactory;

@Component
@Primary
@Profile("integration")
public class FakeCharacterDeckFactory implements CharacterDeckFactory {

	@Override
	public CharacterDeck create() {
		return new FakeCharacterDeck();
	}
	
}
