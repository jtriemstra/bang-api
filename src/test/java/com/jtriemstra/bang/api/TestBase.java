package com.jtriemstra.bang.api;


import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jtriemstra.bang.api.dto.request.ChooseTargetRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.NotificationService;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Missed;
//import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.deck.CardDeck;
import com.jtriemstra.bang.api.model.deck.CharacterDeck;
import com.jtriemstra.bang.api.model.deck.RealCardDeck;
import com.jtriemstra.bang.api.model.deck.RoleDeck;
import com.jtriemstra.bang.api.model.player.PlayerFactoryMock;

public class TestBase {
	protected RealCardDeck createAllBangDeck() {
		RealCardDeck realDeck = new RealCardDeck();
		RealCardDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(new Bang("7", "Diamonds"));
		
		return mockDeck;
	}
	
	protected RealCardDeck createDeckWithFirstCard(Card c) {
		RealCardDeck realDeck = new RealCardDeck();
		RealCardDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenAnswer(new Answer() {
		    protected int count = 0;

		    public Object answer(InvocationOnMock invocation) {
		    	Card result;
		        if (count == 0) {
		        	result = c;
		        }
		        else {
		        	result = new Bang("7", "Diamonds");
		        }
		        
		        count++;

		        return result;
		    }
		});
		
		return mockDeck;
	}
	
	protected RealCardDeck createDeckWithMissed() {
		RealCardDeck realDeck = new RealCardDeck();
		RealCardDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenAnswer(new Answer() {
		    private int count = 0;

		    public Object answer(InvocationOnMock invocation) {
		        if (count++ == 6)
		            return new Missed("7", "Diamonds");

		        return new Bang("7", "Diamonds");
		    }
		});
		
		return mockDeck;
	}
	
	protected RealCardDeck createDeckWithCardsAtIndex(Map<Integer, Card> cards) {
		RealCardDeck realDeck = new RealCardDeck();
		RealCardDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenAnswer(new Answer() {
		    protected int count = 0;

		    public Object answer(InvocationOnMock invocation) {
		    	Card result;
		        if (cards.containsKey(count)) {
		        	result = cards.get(count);
		        }
		        else {
		        	result = new Bang("7", "Diamonds");
		        }
		        
		        count++;

		        return result;
		    }
		});
		
		return mockDeck;
	}
	
	protected Game createMockGame() {
		return createMockGame(createAllBangDeck());
	}
	
	protected Game createMockGame(CardDeck deck) {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		Game game = null;
		when(mockDeck.draw()).thenReturn(new Character("DummySheriff", 4), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4));
		
		game = createMockGame(deck, mockDeck);
		return game;
	}
	
	protected Game createMockGame(CharacterDeck charDeck) {
		return createMockGame(createAllBangDeck(), charDeck);
	}

	protected Game createMockGame(CardDeck deck, CharacterDeck charDeck) {
		RoleDeck roleDeck = Mockito.spy(new RoleDeck());
		Mockito.doReturn(Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW).when(roleDeck).draw();
				
		Game game = new Game("Test1", "Test1", charDeck, deck, Mockito.mock(NotificationService.class), new PlayerFactoryMock(), roleDeck);
		
		game.addPlayer("Test1");
		game.addPlayer("Test2");
		game.addPlayer("Test3");
		game.addPlayer("Test4");
		
		game.start();
		
		return game;
	}
	
	protected DrawRequest createDrawRequest(int numberToDraw, String source) {
		DrawRequest r = new DrawRequest();
		r.setNumberToDraw(numberToDraw);
		r.setSourceName(source);
		return r;
	}
	
	protected PlayRequest createPlayRequest(String cardName, UUID cardId) {
		PlayRequest r = new PlayRequest();
		r.setCardNames(new String[] {cardName});
		r.setCardIds(new UUID[] {cardId});
		return r;
	}
	
	protected PlayRequest createPlayRequest(String[] cardName, UUID[] cardId) {
		PlayRequest r = new PlayRequest();
		r.setCardNames(cardName);
		r.setCardIds(cardId);
		return r;
	}
	
	protected ChooseTargetRequest createTargetRequest(String playerName) {
		ChooseTargetRequest r = new ChooseTargetRequest();
		r.setTargetId(playerName);
		return r;
	}
		
	protected DiscardRequest createDiscardRequest(String cardName, UUID cardId) {
		DiscardRequest r = new DiscardRequest();
		r.setCardNames(new String[] {cardName});
		r.setCardIds(new UUID[] {cardId});
		return r;
	}
	
	protected DiscardRequest createDiscardRequest(String[] cardName, UUID[] cardId) {
		DiscardRequest r = new DiscardRequest();
		r.setCardNames(cardName);
		r.setCardIds(cardId);
		return r;
	}
	
	protected UUID[] createUUIDs(Card... c) {
		UUID[] output = new UUID[c.length];
		for (int i=0; i<c.length; i++) {
			output[i] = createUUID(c[i]);
		}
		return output;		
	}
	
	protected UUID createUUID(Card c) {
		return UUID.nameUUIDFromBytes((c.getName() + " " + c.getDenomination() + " " + c.getSuit()).getBytes());
	}
}
