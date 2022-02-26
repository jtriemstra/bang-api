package com.jtriemstra.bang.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.jtriemstra.bang.api.model.CardDeck;
import com.jtriemstra.bang.api.model.CharacterDeck;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.NotificationService;
import com.jtriemstra.bang.api.model.RealCardDeck;
import com.jtriemstra.bang.api.model.RoleDeck;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.PlayerFactoryMock;

public class DeckTests extends TestBase {
	@Test
	public void when_deck_is_empty_then_reload_from_discard() {
		CardDeck d = new RealCardDeck();
		int initialSize = d.getSize();
		
		Game game = new Game("Test1", "Test1", Mockito.mock(CharacterDeck.class), d, Mockito.mock(NotificationService.class), new PlayerFactoryMock(), Mockito.mock(RoleDeck.class));
		for (int i=0; i<initialSize; i++) {
			Card c = game.draw();
			game.discard(c);
		}
		
		Assertions.assertEquals(0, d.getSize());
		
		Card c = game.draw();
		
		Assertions.assertEquals(initialSize - 1, d.getSize());
	}
}
