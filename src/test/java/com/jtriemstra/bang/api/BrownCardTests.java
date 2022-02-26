package com.jtriemstra.bang.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRuleRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.StealResponse;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Beer;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.CatBalou;
import com.jtriemstra.bang.api.model.card.Gatling;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Panic;
import com.jtriemstra.bang.api.model.card.Saloon;
import com.jtriemstra.bang.api.model.card.Stagecoach;
import com.jtriemstra.bang.api.model.card.WellsFargo;
import com.jtriemstra.bang.api.model.player.Player;

public class BrownCardTests extends TestBase {

	UUID defaultCardId = UUID.nameUUIDFromBytes("Bang 7 Diamonds".getBytes());
	UUID missedCardId = UUID.nameUUIDFromBytes("Missed 7 Diamonds".getBytes());
	
	@Test
	public void when_playing_stagecoach_get_two_cards() {
		Game game = createMockGame(createDeckWithFirstCard(new Stagecoach("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Stagecoach", UUID.nameUUIDFromBytes("Stagecoach 9 Hearts".getBytes())));
		assertEquals("play;discardRule", r.getNextActions().toString());
		assertEquals(8, p0.getHandSize());
	}
	
	@Test
	public void when_playing_wellsfargo_get_three_cards() {
		Game game = createMockGame(createDeckWithFirstCard(new WellsFargo("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Wells Fargo", UUID.nameUUIDFromBytes("Wells Fargo 9 Hearts".getBytes())));
		assertEquals("play;discardRule", r.getNextActions().toString());
		assertEquals(9, p0.getHandSize());
	}
	
	@Test
	public void when_playing_panic_can_steal_from_neighbors_and_victim_gets_message() {
		Game game = createMockGame(createDeckWithFirstCard(new Panic("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Panic", UUID.nameUUIDFromBytes("Panic 9 Hearts".getBytes())));
		assertEquals("chooseTarget", r.getNextActions().toString());
		assertEquals(2, r.getTargets().size());
		assertEquals("Test2", r.getTargets().get(0));
		assertEquals("Test4", r.getTargets().get(1));
		
		BaseResponse r1 = p0.doAction(createTargetRequest("Test2"));
		assertTrue(r1 instanceof StealResponse);
		assertEquals(7, p0.getHandSize());
		assertEquals(2, p1.getHandSize());
		assertEquals("play;discardRule", r1.getNextActions().toString());
		
		r1 = p1.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r1.getNextActions().toString());
	}
	
	@Test
	public void when_playing_cat_balou_can_discard_anyone() {
		Game game = createMockGame(createDeckWithFirstCard(new CatBalou("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Cat Balou", UUID.nameUUIDFromBytes("Cat Balou 9 Hearts".getBytes())));
		assertEquals("chooseTarget", r.getNextActions().toString());
		assertEquals(3, r.getTargets().size());
		
		BaseResponse r1 = p0.doAction(createTargetRequest("Test2"));
		assertEquals(6, p0.getHandSize());
		assertEquals(2, p1.getHandSize());
		assertEquals("play;discardRule", r1.getNextActions().toString());
	}
	
	@Test
	public void when_playing_gatling_everyone_is_targeted() {
		Game game = createMockGame(createDeckWithFirstCard(new Gatling("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Gatling", UUID.nameUUIDFromBytes("Gatling 9 Hearts".getBytes())));
		assertEquals("wait", r.getNextActions().toString());
		WaitingResponse r1 =  (WaitingResponse) p1.doAction(new WaitingRequest());
		WaitingResponse r2 =  (WaitingResponse) p2.doAction(new WaitingRequest());
		WaitingResponse r3 =  (WaitingResponse) p3.doAction(new WaitingRequest());
		
		assertEquals("defenseOptions", r1.getNextActions().toString());
		assertEquals("defenseOptions", r2.getNextActions().toString());
		assertEquals("defenseOptions", r3.getNextActions().toString());
		
	}
	
	@Test
	public void when_playing_gatling_everyone_can_pass() {
		Game game = createMockGame(createDeckWithFirstCard(new Gatling("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		PlayResponse r = (PlayResponse) p0.doAction(createPlayRequest("Gatling", UUID.nameUUIDFromBytes("Gatling 9 Hearts".getBytes())));
		BaseResponse r1 = (BaseResponse) p1.doAction(new DefenseOptionsRequest());
		BaseResponse r2 = (BaseResponse) p2.doAction(new DefenseOptionsRequest());
		BaseResponse r3 = (BaseResponse) p3.doAction(new DefenseOptionsRequest());
		
		assertEquals("wait", r.getNextActions().toString());
		assertEquals("pass", r1.getNextActions().toString());
		assertEquals("pass", r2.getNextActions().toString());
		assertEquals("pass", r3.getNextActions().toString());
	}

	@Test
	public void when_playing_gatling_everyone_passes_and_p0_can_play() {
		Game game = createMockGame(createDeckWithFirstCard(new Gatling("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Gatling", UUID.nameUUIDFromBytes("Gatling 9 Hearts".getBytes())));
		p1.doAction(new DefenseOptionsRequest());
		p2.doAction(new DefenseOptionsRequest());
		p3.doAction(new DefenseOptionsRequest());
				
		p1.doAction(new PassRequest());
		BaseResponse r = p0.doAction(new WaitingRequest());
		assertEquals("wait", r.getNextActions().toString());
		r = p1.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r.getNextActions().toString());
		
		p2.doAction(new PassRequest());
		r = p0.doAction(new WaitingRequest());
		assertEquals("wait", r.getNextActions().toString());
		r = p2.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r.getNextActions().toString());

		p3.doAction(new PassRequest());
		r = p0.doAction(new WaitingRequest());
		assertEquals("play;discardRule", r.getNextActions().toString());
		r = p1.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r.getNextActions().toString());
		r = p2.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r.getNextActions().toString());
		r = p3.doAction(new WaitingRequest());
		assertEquals("wait;dismissMessage", r.getNextActions().toString());
		
		assertEquals(2, p1.getHealth());
		assertEquals(3, p2.getHealth());
		assertEquals(3, p3.getHealth());
	}
	
	@Test
	public void when_playing_gatling_p2_can_defend() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Gatling("9", "Hearts"));
		cards.put(9,  new Missed("K", "Spades"));
		Game game = createMockGame(createDeckWithCardsAtIndex(cards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		BaseResponse r = p0.doAction(createPlayRequest("Gatling", UUID.nameUUIDFromBytes("Gatling 9 Hearts".getBytes())));
		assertEquals("wait", r.getNextActions().toString());
		r = p1.doAction(new DefenseOptionsRequest());
		assertEquals("pass", r.getNextActions().toString());
		r = p2.doAction(new DefenseOptionsRequest());
		assertEquals("play;pass", r.getNextActions().toString());
		r = p3.doAction(new DefenseOptionsRequest());
		assertEquals("pass", r.getNextActions().toString());
		
		p1.doAction(new PassRequest());
		p3.doAction(new PassRequest());
		p2.doAction(createPlayRequest("Missed", UUID.nameUUIDFromBytes("Missed K Spades".getBytes())));
		
		assertEquals(2, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(3, p3.getHealth());
	}
	
	@Test
	public void when_playing_saloon_nobody_goes_above_max_health() {
		Game game = createMockGame(createDeckWithFirstCard(new Saloon("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		p0.doAction(createPlayRequest("Saloon", UUID.nameUUIDFromBytes("Saloon 9 Hearts".getBytes())));

		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
	}
	
	@Test
	public void when_playing_gatling_and_saloon_everyone_is_restored() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Gatling("9", "Hearts"));
		cards.put(1,  new Saloon("K", "Spades"));
		Game game = createMockGame(createDeckWithCardsAtIndex(cards));
		
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Gatling", UUID.nameUUIDFromBytes("Gatling 9 Hearts".getBytes())));
		p1.doAction(new DefenseOptionsRequest());
		p2.doAction(new DefenseOptionsRequest());
		p3.doAction(new DefenseOptionsRequest());
		
		p1.doAction(new PassRequest());
		p2.doAction(new PassRequest());
		p3.doAction(new PassRequest());
		
		assertEquals(2, p1.getHealth());
		assertEquals(3, p2.getHealth());
		assertEquals(3, p3.getHealth());
		
		p0.doAction(createPlayRequest("Saloon", UUID.nameUUIDFromBytes("Saloon K Spades".getBytes())));
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
	}
	
	@Test
	public void when_playing_beer_doesnt_go_above_max_health() {
		Game game = createMockGame(createDeckWithFirstCard(new Beer("9", "Hearts")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		assertThrows(RuntimeException.class, () -> {
			p0.doAction(createPlayRequest("Beer", UUID.nameUUIDFromBytes("Beer 9 Hearts".getBytes())));
	    });
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
	}
	
	@Test
	public void when_playing_beer_adds_health() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(5,  new Beer("9", "Hearts"));
		Game game = createMockGame(createDeckWithCardsAtIndex(cards));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		Player p3 = game.getPlayers().getByIndex(3);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		
		assertEquals(5, p0.getHealth());
		assertEquals(2, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		p1.doAction(new WaitingRequest());
		p1.doAction(createDrawRequest(2, "deck"));
		p1.doAction(createPlayRequest("Beer", UUID.nameUUIDFromBytes("Beer 9 Hearts".getBytes())));
		
		assertEquals(5, p0.getHealth());
		assertEquals(3, p1.getHealth());
		assertEquals(4, p2.getHealth());
		assertEquals(4, p3.getHealth());
		
	}
}
