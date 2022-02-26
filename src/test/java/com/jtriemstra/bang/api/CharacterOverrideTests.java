package com.jtriemstra.bang.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jtriemstra.bang.api.dto.request.BarrelRequest;
import com.jtriemstra.bang.api.dto.request.ChooseCardRequest;
import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRuleRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.JourdonnaisRequest;
import com.jtriemstra.bang.api.dto.request.LuckyDukeRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.ChooseCardResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.model.CharacterDeck;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Barrel;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Gatling;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Volcanic;
import com.jtriemstra.bang.api.model.character.BartCassidy;
import com.jtriemstra.bang.api.model.character.BlackJack;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.character.ElGringo;
import com.jtriemstra.bang.api.model.character.JesseJones;
import com.jtriemstra.bang.api.model.character.Jourdonnais;
import com.jtriemstra.bang.api.model.character.KitCarlson;
import com.jtriemstra.bang.api.model.character.LuckyDuke;
import com.jtriemstra.bang.api.model.character.PaulRegret;
import com.jtriemstra.bang.api.model.character.PedroRamirez;
import com.jtriemstra.bang.api.model.character.RoseDoolan;
import com.jtriemstra.bang.api.model.character.SidKetchum;
import com.jtriemstra.bang.api.model.character.SlabTheKiller;
import com.jtriemstra.bang.api.model.character.SuzyLafayette;
import com.jtriemstra.bang.api.model.character.WillyTheKid;
import com.jtriemstra.bang.api.model.player.Player;

public class CharacterOverrideTests extends TestBase {

	UUID defaultCardId = UUID.nameUUIDFromBytes("Bang 7 Diamonds".getBytes());
	UUID missedCardId = UUID.nameUUIDFromBytes("Missed 7 Diamonds".getBytes());
	
	@Test
	public void when_player_is_pedro_ramirez_but_empty_discard_can_only_draw_deck() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new PedroRamirez(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		
		DrawSourceResponse r = (DrawSourceResponse) p0.doAction(new DrawSourceRequest());
		
		assertEquals(1, r.getSourceNames().size());

//		assertThrows(RuntimeException.class, () -> {
//			p0.doAction(createDrawRequest(1, "discard"));
//	    });
		
		BaseResponse r1 = p0.doAction(createDrawRequest(2, "deck"));
		//p0.doAction(new DrawSourceRequest());
		//BaseResponse r1 = p0.doAction(createDrawRequest(1, "deck"));
		assertEquals(7, p0.getHandSize());
		assertEquals("play;discardRule", r1.getNextActions().toString());
		
	}
	
	@Test
	public void when_player_is_pedro_ramirez_can_draw_discard_if_not_empty() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy3", 3), new PedroRamirez(), new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		Player p2 = game.getPlayers().getByIndex(2);
		
//		p0.doAction(new DrawSourceRequest());
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test3"));
		p2.doAction(new DefenseOptionsRequest());
		p2.doAction(new PassRequest());
		p0.doAction(new DiscardRuleRequest());
		p0.doAction(createDiscardRequest("Bang", defaultCardId));
		
		assertEquals(4, p1.getHandSize());
		DrawSourceResponse r = (DrawSourceResponse) p1.doAction(new DrawSourceRequest());
		assertEquals(2, r.getSourceNames().size());
		assertEquals("deck", r.getSourceNames().get(0));
		assertEquals("discard", r.getSourceNames().get(1));
		
		BaseResponse r1 = p1.doAction(createDrawRequest(1, "discard"));
		assertEquals(5, p1.getHandSize());
		
		assertEquals("draw", r1.getNextActions().toString());
//		assertEquals("drawSource", r1.getNextActions().toString());
//		r = (DrawSourceResponse) p1.doAction(new DrawSourceRequest());
//		assertEquals(1, r.getSourceNames().size());
//		assertEquals("deck", r.getSourceNames().get(0));
//		
//		assertThrows(RuntimeException.class, () -> {
//			p1.doAction(createDrawRequest(1, "discard"));
//	    });
		
		r1 = p1.doAction(createDrawRequest(1, "deck"));
		assertEquals(6, p1.getHandSize());
		assertEquals("play;discardRule", r1.getNextActions().toString());
		
	}
	
	@Test
	public void when_player_is_not_pedro_ramirez_then_can_draw_from_deck() {
		Game game = createMockGame();
		Player p = game.getPlayers().getByIndex(0);
		
		DrawResponse r = (DrawResponse) p.doAction(new DrawRequest().setNumberToDraw(2).setSourceName("deck"));
		
		assertEquals(2, r.getCards().size());
	}
	
	@Test
	public void when_player_is_rose_doolan_then_can_target_more() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new RoseDoolan(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		
		p0.doAction(createDrawRequest(2, "deck"));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		
		assertEquals(3, r.getTargets().size());
		
	}
	
	@Test
	public void when_player_is_paul_regret_then_is_harder_to_target() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("DummySheriff", 4), new PaulRegret() ,new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		
		p0.doAction(createDrawRequest(2, "deck"));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		assertEquals(1, r.getTargets().size());
		
	}
	
	@Test
	public void when_player_is_bart_cassidy_then_draw_on_hit() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("DummySheriff", 4), new BartCassidy() ,new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		
		assertEquals(5, p1.getHandSize());
	}
	
	@Test
	public void when_player_is_slab_the_killer_then_cant_dodge_with_1_missed() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(5,  new Missed("K", "Spades"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SlabTheKiller(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		BaseResponse r = p1.doAction(new DefenseOptionsRequest());
		assertEquals("pass", r.getNextActions().toString());		
	}
	
	@Test
	public void when_player_is_slab_the_killer_then_must_play_2_missed_to_dodge() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(5,  new Missed("K", "Spades"));
		cards.put(6,  new Missed("Q", "Spades"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SlabTheKiller(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p1.doAction(new DefenseOptionsRequest());
		assertEquals("play;pass", r.getNextActions().toString());
		
		assertThrows(RuntimeException.class, () -> {
			p1.doAction(createPlayRequest("Missed", createUUID(cards.get(5))));
	    });
		//assertEquals("play;pass", r.getNextActions().toString());
		
		assertThrows(RuntimeException.class, () -> {
			p1.doAction(createPlayRequest(new String[]{"Missed", "Bang"}, new UUID[] {createUUID(cards.get(5)), defaultCardId}));
	    });
		//assertEquals("play;pass", r.getNextActions().toString());
		
		r = p1.doAction(createPlayRequest(new String[]{"Missed", "Missed"}, createUUIDs(cards.get(5), cards.get(6))));
		assertEquals(3, p1.getHealth());
		assertEquals(1, p1.getHandSize());
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new WaitingRequest());
		assertEquals("play;discardRule", r.getNextActions().toString());
		
		
	}
	
	@Test
	public void when_player_is_suzy_lafayette_then_get_extra_card_after_playing_all_on_defense() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Volcanic("K", "Hearts"));
		cards.put(5,  new Missed("K", "Spades"));
		cards.put(6,  new Missed("Q", "Spades"));
		cards.put(7,  new Missed("J", "Spades"));
		cards.put(8,  new Missed("10", "Spades"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SlabTheKiller(), new SuzyLafayette(),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Volcanic", createUUID(cards.get(0))));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p1.doAction(new DefenseOptionsRequest());
		r = p1.doAction(createPlayRequest(new String[]{"Missed", "Missed"}, createUUIDs(cards.get(5), cards.get(6))));
		assertEquals(4, p1.getHealth());
		assertEquals(2, p1.getHandSize());
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new WaitingRequest());
		assertEquals("play;discardRule", r.getNextActions().toString());
		
		
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		r = p0.doAction(createTargetRequest("Test2"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p1.doAction(new DefenseOptionsRequest());
		r = p1.doAction(createPlayRequest(new String[]{"Missed", "Missed"}, createUUIDs(cards.get(7), cards.get(8))));
		assertEquals(4, p1.getHealth());
		assertEquals(1, p1.getHandSize());
		//assertEquals("Bang", p1.getHand()[0]);
	}
	
	@Test
	public void when_player_is_suzy_lafayette_then_get_extra_card_after_playing_all_on_offense() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(5,  new Volcanic("K", "Hearts"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SlabTheKiller(), new SuzyLafayette(),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(new DiscardRuleRequest());
		BaseResponse r = p0.doAction(createDiscardRequest(new String[]{"Bang", "Bang"}, new UUID[] {defaultCardId, defaultCardId}));
		assertEquals("wait", r.getNextActions().toString());
		
		assertEquals(4, p1.getHandSize());
		p1.doAction(createDrawRequest(2, "deck"));
		assertEquals(6, p1.getHandSize());
		
		p1.doAction(createPlayRequest("Volcanic", createUUID(cards.get(5))));
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		p0.doAction(new DefenseOptionsRequest());
		p0.doAction(new PassRequest());
		assertEquals(4, p1.getHandSize());
				
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		p0.doAction(new DefenseOptionsRequest());
		p0.doAction(new PassRequest());
		assertEquals(3, p1.getHandSize());
		
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		p0.doAction(new DefenseOptionsRequest());
		p0.doAction(new PassRequest());
		assertEquals(2, p1.getHandSize());
		
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		p0.doAction(new DefenseOptionsRequest());
		p0.doAction(new PassRequest());
		assertEquals(1, p1.getHandSize());
		
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		p1.doAction(createTargetRequest("Test1"));
		p0.doAction(new DefenseOptionsRequest());
		p0.doAction(new PassRequest());
		assertEquals(1, p1.getHandSize());
		
	}
	
	@Test
	public void when_player_is_el_gringo_then_get_card_on_bang_hit() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Volcanic("K", "Hearts"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy", 4), new ElGringo(), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(3, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		assertEquals(7, p0.getHandSize());
		
		p0.doAction(createPlayRequest("Volcanic", createUUID(cards.get(0))));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals("wait", r.getNextActions().toString());
		assertEquals(5, p0.getHandSize());
		
		r = p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		assertEquals(4, p1.getHandSize());
		assertEquals(4, p0.getHandSize());
		assertEquals(2, p1.getHealth());
		
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		assertEquals(5, p1.getHandSize());
		assertEquals(2, p0.getHandSize());
		assertEquals(1, p1.getHealth());
	}
	
	@Test
	public void when_player_is_el_gringo_then_get_card_on_gatling_hit() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Gatling("K", "Hearts"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy", 4), new ElGringo(),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(3, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		assertEquals(7, p0.getHandSize());
		
		BaseResponse r = p0.doAction(createPlayRequest("Gatling", createUUID(cards.get(0))));
		assertEquals("wait", r.getNextActions().toString());
		assertEquals(6, p0.getHandSize());
		
		r = p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		assertEquals(2, p1.getHealth());
		
	}
	
	@Test
	public void when_player_is_jourdonnais_then_has_extra_barrel_option() {
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy", 4), new Jourdonnais(),new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p1.doAction(new DefenseOptionsRequest());
		assertEquals("pass;jourdonnais", r.getNextActions().toString());
	}
	
	@Test
	public void when_player_is_jourdonnais_then_can_defend_many_times() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Barrel("A", "Hearts"));
		cards.put(17,  new Gatling("K", "Hearts"));
		cards.put(18,  new Bang("Q", "Hearts"));
		cards.put(21,  new Bang("Q", "Spades"));
		cards.put(22,  new Bang("J", "Hearts"));
		
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Jourdonnais(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Barrel", createUUID(cards.get(0))));
		p0.doAction(new DiscardRuleRequest());
		BaseResponse r = p0.doAction(createDiscardRequest("Bang", defaultCardId));
		assertEquals("wait", r.getNextActions().toString());
		
		p1.doAction(createDrawRequest(2, "deck"));
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		r = p1.doAction(createTargetRequest("Test1"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass;jourdonnais", r.getNextActions().toString());
		p0.doAction(new JourdonnaisRequest());
		assertEquals("barrel;pass", r.getNextActions().toString());
		r = p0.doAction(new BarrelRequest());
		assertEquals("wait", r.getNextActions().toString());
		
	}
	
	@Test
	public void when_player_is_jourdonnais_then_heart_is_defense() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Barrel("A", "Hearts"));
		cards.put(17,  new Gatling("K", "Hearts"));
		cards.put(18,  new Bang("Q", "Hearts"));
		cards.put(21,  new Bang("J", "Hearts"));
	
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Jourdonnais(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Barrel", createUUID(cards.get(0))));
		p0.doAction(new DiscardRuleRequest());
		BaseResponse r = p0.doAction(createDiscardRequest("Bang", defaultCardId));
		assertEquals("wait", r.getNextActions().toString());
		
		p1.doAction(createDrawRequest(2, "deck"));
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		r = p1.doAction(createTargetRequest("Test1"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass;jourdonnais", r.getNextActions().toString());
		r = p0.doAction(new JourdonnaisRequest());
		assertEquals("wait", r.getNextActions().toString());
		
	}
	
	
	@Test()
	public void when_player_is_willy_can_play_multiple_bang() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new WillyTheKid(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		p0.doAction(createPlayRequest("Bang", defaultCardId));

		
	}
	
	@Test()
	public void when_player_is_jesse_jones_has_multiple_draw_sources_on_first_card() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new JesseJones(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		DrawSourceResponse r = (DrawSourceResponse) p0.doAction(new DrawSourceRequest());
		
		assertEquals("draw", r.getNextActions().toString());
		assertEquals(4, r.getSourceNames().size());
		assertEquals("deck", r.getSourceNames().get(0));
		assertEquals("Test2", r.getSourceNames().get(1));		
	}
	
	@Test()
	public void when_player_is_jesse_jones_gets_one_card_per_draw() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new JesseJones(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		DrawSourceResponse r = (DrawSourceResponse) p0.doAction(new DrawSourceRequest());
		
		assertEquals("draw", r.getNextActions().toString());
		assertEquals(4, r.getSourceNames().size());
		assertEquals("deck", r.getSourceNames().get(0));
		assertEquals("Test2", r.getSourceNames().get(1));		
		assertEquals(1, r.getNumberToDraw());
		
		BaseResponse r1 = p0.doAction(new DrawRequest().setSourceName("deck").setNumberToDraw(1));
		assertEquals("draw", r1.getNextActions());
		r1 = p0.doAction(new DrawRequest().setSourceName("deck").setNumberToDraw(1));
		
		assertEquals(7, p0.getHandSize());
	}
	
	@Test()
	public void when_player_is_kit_carlson_gets_three_cards_to_choose_two() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new KitCarlson(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		ChooseCardResponse r1 = (ChooseCardResponse) p0.doAction(new DrawRequest().setSourceName("deck").setNumberToDraw(3));
		
		assertEquals("chooseCard", r1.getNextActions());
		assertEquals(2, r1.getNumberToChoose());
		
		
		p0.doAction(new ChooseCardRequest().setCardNames(new String[] {"Bang", "Bang"}).setCardIds(new UUID[] {defaultCardId, defaultCardId}));
	}
	
	@Test
	public void when_player_is_lucky_duke_then_can_draw_two() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Barrel("A", "Hearts"));
		cards.put(17,  new Gatling("K", "Hearts"));
		cards.put(18,  new Bang("Q", "Hearts"));
		cards.put(21,  new Bang("J", "Hearts"));
	
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new LuckyDuke(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Barrel", createUUID(cards.get(0))));
		p0.doAction(new DiscardRuleRequest());
		BaseResponse r = p0.doAction(createDiscardRequest("Bang", defaultCardId));
		assertEquals("wait", r.getNextActions().toString());
		
		p1.doAction(createDrawRequest(2, "deck"));
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		r = p1.doAction(createTargetRequest("Test1"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass", r.getNextActions().toString());
		r = p0.doAction(new BarrelRequest());
		assertEquals("barrel", r.getNextActions().toString());		
		assertEquals(2, ((DrawResponse)r).getCards().size());
		
		List<Card> drawnCards = ((DrawResponse)r).getCards();
		
		r = p0.doAction(new LuckyDukeRequest().setUseId(drawnCards.get(0).getId()).setDiscardId(drawnCards.get(1).getId()).setAction("barrel"));
		assertEquals("wait", r.getNextActions().toString());
	}
	
	@Test
	public void when_player_is_lucky_duke_then_can_draw_two_but_need_heart() {
		HashMap<Integer, Card> cards = new HashMap<>();
		cards.put(0,  new Barrel("A", "Hearts"));
		cards.put(17,  new Gatling("K", "Diamonds"));
		cards.put(18,  new Bang("Q", "Diamonds"));
		cards.put(21,  new Bang("J", "Diamonds"));
	
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new LuckyDuke(), new Character("Dummy", 4), new Character("Dummy", 4), new Character("Dummy", 4));
		
		Game game = createMockGame(createDeckWithCardsAtIndex(cards), mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		assertEquals(4, p1.getHandSize());
		assertEquals(5, p0.getHandSize());
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Barrel", createUUID(cards.get(0))));
		p0.doAction(new DiscardRuleRequest());
		BaseResponse r = p0.doAction(createDiscardRequest("Bang", defaultCardId));
		assertEquals("wait", r.getNextActions().toString());
		
		p1.doAction(createDrawRequest(2, "deck"));
		p1.doAction(createPlayRequest("Bang", defaultCardId));
		r = p1.doAction(createTargetRequest("Test1"));
		assertEquals("wait", r.getNextActions().toString());
		
		r = p0.doAction(new DefenseOptionsRequest());
		assertEquals("barrel;pass", r.getNextActions().toString());
		r = p0.doAction(new BarrelRequest());
		assertEquals("barrel", r.getNextActions().toString());		
		assertEquals(2, ((DrawResponse)r).getCards().size());

		List<Card> drawnCards = ((DrawResponse)r).getCards();
		
		r = p0.doAction(new LuckyDukeRequest().setUseId(drawnCards.get(0).getId()).setDiscardId(drawnCards.get(1).getId()).setAction("barrel"));
		assertEquals("pass", r.getNextActions().toString());
	}

	@Test
	public void when_player_is_sid_ketchum_has_extra_action_on_turn() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SidKetchum(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		p0.loseHealthAndCheckEndGame(game.getPlayers().getByIndex(1));
		
		DrawResponse r1 = (DrawResponse) p0.doAction(createDrawRequest(2, "deck"));
		
		Assertions.assertTrue(r1.getNextActions().contains("discardRuleSid"));
		
	}

	@Test
	public void when_player_is_sid_ketchum_with_max_health_has_no_extra_permanent_action() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy3", 3), new SidKetchum(), new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p1 = game.getPlayers().getByIndex(1);
		
		BaseResponse r = p1.doAction(new WaitingRequest());
		
		Assertions.assertEquals("wait", r.getNextActions());
		
	}

	@Test
	public void when_player_is_sid_ketchum_with_less_health_has_no_extra_permanent_action() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new Character("Dummy3", 3), new SidKetchum(), new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p1 = game.getPlayers().getByIndex(1);
		p1.loseHealthAndCheckEndGame(game.getPlayers().getByIndex(0));
		
		BaseResponse r = p1.doAction(new WaitingRequest());
		
		Assertions.assertEquals("wait;dismissMessage;discardRuleSid", r.getNextActions());
		
	}
	
	@Test
	public void when_player_is_sid_ketchum_can_discard_for_health() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new SidKetchum(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		p0.loseHealthAndCheckEndGame(game.getPlayers().getByIndex(1));
		
		DrawResponse r1 = (DrawResponse) p0.doAction(createDrawRequest(2, "deck"));
		
		Assertions.assertTrue(r1.getNextActions().contains("discardRuleSid"));
		Assertions.assertEquals(4, p0.getHealth());
		
		BaseResponse r2 = p0.doAction(new DiscardRuleRequest().setActionName("discardRuleSid")); 
				
		p0.doAction(new DiscardRequest().setCardNames(new String[] {"Bang","Bang"}).setCardIds(new UUID[] {defaultCardId, defaultCardId}));
		
		Assertions.assertEquals(5, p0.getHealth());
	}

	@Test
	public void when_player_is_black_jack_can_get_extra_card() {
		CharacterDeck realDeck = new CharacterDeck();
		CharacterDeck mockDeck = spy(realDeck);
		when(mockDeck.draw()).thenReturn(
				new BlackJack(), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
			);
		
		Game game = createMockGame(mockDeck);
		Player p0 = game.getPlayers().getByIndex(0);
		
		Assertions.assertEquals(5, p0.getHandSize());
		
		DrawResponse r1 = (DrawResponse) p0.doAction(createDrawRequest(2, "deck"));
		
		Assertions.assertEquals(8, p0.getHandSize());
	}
}
