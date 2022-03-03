package com.jtriemstra.bang.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DefenseOptionsResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.deck.CharacterDeck;
import com.jtriemstra.bang.api.model.deck.RealCardDeck;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Card;



public class BasicGameplayTests extends TestBase {
	
	UUID defaultCardId = UUID.nameUUIDFromBytes("Bang 7 Diamonds".getBytes());
	UUID missedCardId = UUID.nameUUIDFromBytes("Missed 7 Diamonds".getBytes());

	@Test
	public void when_starting_game_card_count_matches_health() {
		
		Game game = createMockGame();
		
		assertEquals(5, game.getPlayers().getByIndex(0).getHandSize());
		assertEquals(3, game.getPlayers().getByIndex(1).getHandSize());
		assertEquals(4, game.getPlayers().getByIndex(2).getHandSize());
	}
	
	@Test
	public void when_drawing_get_two_extra_cards() {
		Game game = createMockGame();
		
		DrawResponse r = (DrawResponse) game.getPlayers().getByIndex(0).doAction(new DrawRequest().setNumberToDraw(2).setSourceName("deck"));
		assertEquals(2, r.getCards().size());
	}
	
	@Test
	public void when_shooting_can_only_target_neighbors() {
		Game game = createMockGame();
		
		Player p0 = game.getPlayers().getByIndex(0);
		p0.doAction(createDrawRequest(2, "deck"));
		
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		assertEquals(6, game.getPlayers().getByIndex(0).getHandSize());
		assertEquals("chooseTarget", r.getNextActions().toString());
		assertEquals(2, r.getTargets().size());
		assertEquals("Test2", r.getTargets().get(0));
		assertEquals("Test4", r.getTargets().get(1));
	}
	
	@Test
	public void when_shooting_target_cannot_dodge() {
		Game game = createMockGame();
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals(6, p0.getHandSize());
		assertEquals("wait", r.getNextActions().toString());
		
		DefenseOptionsResponse r2 = (DefenseOptionsResponse) p1.doAction(new DefenseOptionsRequest());
		assertEquals(0, r2.getValidCards().size());
		assertEquals("pass", r2.getNextActions().toString());
		
		BaseResponse r3 = p1.doAction(new PassRequest());
		assertEquals(2, p1.getHealth());
		assertEquals(3, p1.getHandSize());
		assertEquals("wait;dismissMessage", r3.getNextActions().toString());
		
		WaitingResponse r4 = (WaitingResponse) p0.doAction(new WaitingRequest());
		assertEquals("play;discardRule", r4.getNextActions().toString());
		
		
	}
	
	@Test
	public void when_shooting_target_cannot_dodge_with_invalid_action() {
		Game game = createMockGame();
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		assertEquals(6, p0.getHandSize());
		assertEquals("wait", r.getNextActions().toString());
		
		DefenseOptionsResponse r2 = (DefenseOptionsResponse) p1.doAction(new DefenseOptionsRequest());
		assertEquals(0, r2.getValidCards().size());		
		assertEquals("pass", r2.getNextActions().toString());
		
		assertThrows(RuntimeException.class, () -> {
			p1.doAction(createPlayRequest("Missed", missedCardId));
	    });		
	}
	
	@Test
	public void when_shooting_target_can_dodge() {
		Game game = createMockGame(createDeckWithMissed());
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		DefenseOptionsResponse r1 = (DefenseOptionsResponse) p1.doAction(new DefenseOptionsRequest());
		assertEquals(1, r1.getValidCards().size());
		assertEquals("play;pass", r1.getNextActions().toString());
		
		p1.doAction(createPlayRequest("Missed", missedCardId));
		assertEquals(3, p1.getHealth());
		assertEquals(2, p1.getHandSize());
	}
	
	@Test
	public void when_shooting_target_cannot_dodge_with_invalid_card() {
		Game game = createMockGame(createDeckWithMissed());
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		
		BaseResponse r = p0.doAction(createTargetRequest("Test2"));
		DefenseOptionsResponse r1 = (DefenseOptionsResponse) p1.doAction(new DefenseOptionsRequest());
		assertEquals(1, r1.getValidCards().size());
		assertEquals("play;pass", r1.getNextActions().toString());
		
		assertThrows(RuntimeException.class, () -> {
			p1.doAction(createPlayRequest("Bang", defaultCardId));
	    });
		
		assertEquals(3, p1.getHealth());
		assertEquals(3, p1.getHandSize());
	}
	
	@Test()
	public void when_shooting_can_only_play_one_bang() {
		Game game = createMockGame();
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		
		assertThrows(RuntimeException.class, () -> {
			p0.doAction(createPlayRequest("Bang", defaultCardId));
	    });
		
	}
}
