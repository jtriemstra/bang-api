package com.jtriemstra.bang.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jtriemstra.bang.api.dto.request.BarrelRequest;
import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRuleRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.BarrelResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Barrel;
import com.jtriemstra.bang.api.model.card.Beer;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Mustang;
import com.jtriemstra.bang.api.model.card.Panic;
import com.jtriemstra.bang.api.model.card.Schofield;
import com.jtriemstra.bang.api.model.card.Scope;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.deck.CharacterDeck;
import com.jtriemstra.bang.api.model.deck.RealCardDeck;


public class TableCardTests extends TestBase {

	UUID defaultCardId = UUID.nameUUIDFromBytes("Bang 7 Diamonds".getBytes());
	UUID missedCardId = UUID.nameUUIDFromBytes("Missed 7 Diamonds".getBytes());

	@Test
	public void when_using_scope_can_target_more() {
		Game game = createMockGame(createDeckWithFirstCard(new Scope("A", "Spades")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Scope", createUUID(new Scope("A", "Spades"))));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		assertEquals(3, r.getTargets().size());
	}
	
	@Test
	public void when_using_mustang_can_avoid_target() {
		Game game = createMockGame(createDeckWithFirstCard(new Mustang("9", "Hearts")));
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Mustang", createUUID(new Mustang("9", "Hearts"))));
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		p1.doAction(createDrawRequest(2, "deck"));
		
		TargetingCardResponse r = (TargetingCardResponse) p1.doAction(createPlayRequest("Bang", defaultCardId));
		
		assertEquals(1, r.getTargets().size());
	}
	
	@Test
	public void when_using_barrel_no_heart_doesnt_miss() {
		
		Game game = createMockGame(createDeckWithFirstCard(new Barrel("Q", "Spades")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Barrel", createUUID(new Barrel("Q", "Spades"))));
		assertEquals("play;discardRule", r.getNextActions().toString());
		
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		p1.doAction(createDrawRequest(2, "deck"));
		
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		
		BaseResponse r1 = p0.doAction(new WaitingRequest()); 
		assertEquals("defenseOptions",r1.getNextActions().toString());
		
		BaseResponse r2 = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass", r2.getNextActions().toString());
		
		BarrelResponse r3 = (BarrelResponse) p0.doAction(new BarrelRequest());
		assertEquals("pass", r3.getNextActions().toString());
		r1 = p1.doAction(new WaitingRequest());
		assertEquals("wait", r1.getNextActions().toString());
		
		assertEquals(5, p0.getHandSize());
		assertEquals(5, p0.getHealth());
	}
	
	@Test
	public void when_using_barrel_heart_misses() {
		HashMap<Integer, Card> forcedCards = new HashMap<>();
		forcedCards.put(0, new Barrel("Q", "Spades"));
		forcedCards.put(20, new Beer("7", "Hearts"));
		Game game = createMockGame(createDeckWithCardsAtIndex(forcedCards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Barrel", createUUID(forcedCards.get(0))));
		assertEquals("play;discardRule", r.getNextActions().toString());
		
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		p1.doAction(createDrawRequest(2, "deck"));
		
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		
		BaseResponse r1 = p0.doAction(new WaitingRequest()); 
		assertEquals("defenseOptions",r1.getNextActions().toString());
		
		BaseResponse r2 = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass", r2.getNextActions().toString());
		
		BarrelResponse r3 = (BarrelResponse) p0.doAction(new BarrelRequest());
		assertEquals("wait", r3.getNextActions().toString());
		assertEquals(5, p0.getHealth());
		
		r1 = p1.doAction(new WaitingRequest());
		assertEquals("play;discardRule", r1.getNextActions().toString());
	}
	
	@Test
	public void when_using_scope_panic_range_expands() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Scope("A", "Spades"));
		cards.put(1,  new Panic("K", "Spades"));
		Game game = createMockGame(this.createDeckWithCardsAtIndex(cards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Scope", createUUID(cards.get(0))));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Panic", createUUID(cards.get(1))));
		assertEquals(3, r.getTargets().size());		
	}
	
	@Test
	public void when_using_schofield_panic_range_does_not_expand() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Schofield("A", "Spades"));
		cards.put(1,  new Panic("K", "Spades"));
		Game game = createMockGame(createDeckWithCardsAtIndex(cards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Schofield", createUUID(cards.get(0))));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Panic", createUUID(cards.get(1))));
		assertEquals(2, r.getTargets().size());
	}
	
	@Test
	public void when_using_mustang_can_avoid_panic() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Mustang("A", "Spades"));
		cards.put(5,  new Panic("K", "Spades"));
		Game game = createMockGame(createDeckWithCardsAtIndex(cards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Mustang", createUUID(cards.get(0))));
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		p1.doAction(createDrawRequest(2, "deck"));
		
		TargetingCardResponse r = (TargetingCardResponse) p1.doAction(createPlayRequest("Panic", createUUID(cards.get(5))));
		
		assertEquals(1, r.getTargets().size());
	}
}
