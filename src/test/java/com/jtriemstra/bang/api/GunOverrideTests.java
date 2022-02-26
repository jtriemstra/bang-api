package com.jtriemstra.bang.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.jtriemstra.bang.api.model.CharacterDeck;
import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.RealCardDeck;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Schofield;
import com.jtriemstra.bang.api.model.card.Volcanic;


public class GunOverrideTests extends TestBase {
	UUID defaultCardId = UUID.nameUUIDFromBytes("Bang 7 Diamonds".getBytes());
	
	@Test
	public void when_shooting_schofield_can_target_more() {
		Game game = createMockGame(createDeckWithFirstCard(new Schofield("Q", "Clubs")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Schofield", UUID.nameUUIDFromBytes("Schofield Q Clubs".getBytes())));
		TargetingCardResponse r = (TargetingCardResponse) p0.doAction(createPlayRequest("Bang", defaultCardId));
		assertEquals(5, game.getPlayers().getByIndex(0).getHandSize());
		assertEquals("chooseTarget", r.getNextActions().toString());
		assertEquals(3, r.getTargets().size());
		
	}
	
	@Test()
	public void when_shooting_volcanic_can_play_multiple_bang() {
		Game game = createMockGame(createDeckWithFirstCard(new Volcanic("10", "Clubs")));
		
		Player p0 = game.getPlayers().getByIndex(0);
		Player p1 = game.getPlayers().getByIndex(1);
		
		p0.doAction(createDrawRequest(2, "deck"));
		
		p0.doAction(createPlayRequest("Volcanic", UUID.nameUUIDFromBytes("Volcanic 10 Clubs".getBytes())));
		p0.doAction(createPlayRequest("Bang", defaultCardId));
		p0.doAction(createTargetRequest("Test2"));
		p1.doAction(new DefenseOptionsRequest());
		p1.doAction(new PassRequest());
		p0.doAction(new WaitingRequest());
		p0.doAction(createPlayRequest("Bang", defaultCardId));

		
	}
}
