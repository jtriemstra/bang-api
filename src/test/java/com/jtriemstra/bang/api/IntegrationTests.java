package com.jtriemstra.bang.api;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtriemstra.bang.api.dto.request.ChooseCardRequest;
import com.jtriemstra.bang.api.dto.request.ChooseTargetRequest;
import com.jtriemstra.bang.api.dto.request.CreateRequest;
import com.jtriemstra.bang.api.dto.request.DefenseOptionsRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRuleRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.JoinRequest;
import com.jtriemstra.bang.api.dto.request.PassRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.request.StartRequest;
import com.jtriemstra.bang.api.dto.request.WaitingRequest;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Barrel;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Duel;
import com.jtriemstra.bang.api.model.card.GeneralStore;
import com.jtriemstra.bang.api.model.card.Indians;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Winchester;
import com.jtriemstra.bang.api.model.deck.RoleDeck;

@SuppressWarnings(value="unchecked")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BangApiApplication.class)
@TestMethodOrder(value = MethodOrderer.Alphanumeric.class)
@ActiveProfiles("integration")
public class IntegrationTests {
	
	protected UUID createUUID(Card c) {
		return UUID.nameUUIDFromBytes((c.getName() + " " + c.getDenomination() + " " + c.getSuit()).getBytes());
	}

	protected UUID[] createUUIDs(Card... c) {
		UUID[] output = new UUID[c.length];
		for (int i=0; i<c.length; i++) {
			output[i] = createUUID(c[i]);
		}
		return output;		
	}
	
	@LocalServerPort
	private int port;

	private WebClient webClient;
	
	@Autowired
	IntegrationTestState state;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@BeforeEach
	public void init() {
		webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
	}
	
	@Test
	public void a001_create() {
		CreateRequest request = new CreateRequest().setPlayerName("test1");
		
		webClient.post().uri("/create").bodyValue(request).retrieve().bodyToMono(Object.class).block();
	}

	@Test
	public void a002_join() {
		webClient.post().uri("/join").bodyValue(
				new JoinRequest().setGameName("test1").setPlayerName("test2")
			).retrieve().bodyToMono(Object.class).block();
		webClient.post().uri("/join").bodyValue(
				new JoinRequest().setGameName("test1").setPlayerName("test3")
			).retrieve().bodyToMono(Object.class).block();
		webClient.post().uri("/join").bodyValue(
				new JoinRequest().setGameName("test1").setPlayerName("test4")
			).retrieve().bodyToMono(Object.class).block();
	}

	@Test
	public void a003_start() {
		webClient.post().uri("/start").bodyValue(
				new StartRequest().setGameName("test1")
			).retrieve().bodyToMono(Object.class).block();
	}
	
	@Test
	public void a004_initial_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("draw", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
		Arrays.asList(wait1, wait2, wait3, wait4).forEach(w -> {
			Map x = (Map) w.get("state");
			Map y = (Map) x.get("currentPlayer");
			Map z = (Map) y.get("hand");
			System.out.println(z.get("cards"));
		});
	}
	
	@Test
	public void a006_turn1_draw() {
		Map<String, Object> draw = webClient.post().uri("/draw").bodyValue(
				new DrawRequest().setSourceName("deck").setNumberToDraw(2).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)draw.get("cards")).size());
		Assertions.assertEquals("play;discardRule", draw.get("nextActions"));
		
		state.setLastCards(((List<LinkedHashMap>) draw.get("cards")).stream().map(c -> c.get("name").toString()).collect(Collectors.toList()));
	}

	@Test
	public void a007_turn1_discardRule() {
		Map<String, Object> discard = webClient.post().uri("/discardRule").bodyValue(
				new DiscardRuleRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals(2, discard.get("numberToDiscard"));
	}

	@Test
	public void a008_turn1_discard() {
		String[] cardNames = new String[state.getLastCards().size()];
		for (int i=0; i<cardNames.length; i++) cardNames[i] = state.getLastCards().get(i);
		
		Map<String, Object> discard = webClient.post().uri("/discard").bodyValue(
				new DiscardRequest().setCardNames(cardNames).setCardIds(createUUIDs(new Bang("Q", "Hearts"), new Bang("6", "Diamonds"))).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", discard.get("nextActions"));
	}

	@Test
	public void a009_turn1_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", wait1.get("nextActions"));
		Assertions.assertEquals("draw", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a011_turn2_draw() {
		Map<String, Object> draw = webClient.post().uri("/draw").bodyValue(
				new DrawRequest().setSourceName("deck").setNumberToDraw(2).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)draw.get("cards")).size());
		Assertions.assertEquals("play;discardRule", draw.get("nextActions"));
		
		state.setLastCards(((List<LinkedHashMap>) draw.get("cards")).stream().map(c -> c.get("name").toString()).collect(Collectors.toList()));
	}

	@Test
	public void a012_turn2_play() {
		Map<String, Object> target = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("10", "Diamonds"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("chooseTarget", target.get("nextActions"));	
		Assertions.assertTrue(target.get("targets") instanceof List);
		Assertions.assertEquals(2, ((List)target.get("targets")).size());
	}

	@Test
	public void a013_turn2_chooseTarget() {
		Map<String, Object> target = webClient.post().uri("/chooseTarget").bodyValue(
				new ChooseTargetRequest().setTargetId("test1").setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", target.get("nextActions"));		
	}

	@Test
	public void a014_turn2_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("defenseOptions", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a015_turn2_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("pass", target.get("nextActions"));		
	}

	@Test
	public void a016_turn2_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("pass", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a017_turn2_pass() {
		Map<String, Object> target = webClient.post().uri("/pass").bodyValue(
				new PassRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", target.get("nextActions"));		
	}

	@Test
	public void a018_turn2_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		Assertions.assertEquals("play;discardRule", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a019_turn2_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Barrel"}).setCardIds(createUUIDs(new Barrel("7", "Diamonds"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;discardRule", play.get("nextActions"));
	}

	@Test
	public void a020_turn2_discardRule() {
		Map<String, Object> discard = webClient.post().uri("/discardRule").bodyValue(
				new DiscardRuleRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals(0, discard.get("numberToDiscard"));
	}

	@Test
	public void a021_turn2_discard() {
				
		Map<String, Object> discard = webClient.post().uri("/discard").bodyValue(
				new DiscardRequest().setCardNames(new String[] {}).setCardIds(new UUID[] {}).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", discard.get("nextActions"));
	}

	@Test
	public void a022_turn2_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("draw", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a024_turn3_draw() {
		Map<String, Object> draw = webClient.post().uri("/draw").bodyValue(
				new DrawRequest().setSourceName("deck").setNumberToDraw(2).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)draw.get("cards")).size());
		Assertions.assertEquals("play;discardRule", draw.get("nextActions"));
		
		state.setLastCards(((List<LinkedHashMap>) draw.get("cards")).stream().map(c -> c.get("name").toString()).collect(Collectors.toList()));
	}

	@Test
	public void a025_turn3_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Winchester"}).setCardIds(createUUIDs(new Winchester("8", "Clubs"))).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;discardRule", play.get("nextActions"));
	}

	@Test
	public void a026_turn3_play() {
		Map<String, Object> target = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("6", "Clubs"))).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("chooseTarget", target.get("nextActions"));	
		Assertions.assertTrue(target.get("targets") instanceof List);
		Assertions.assertEquals(3, ((List)target.get("targets")).size());
	}

	@Test
	public void a027_turn3_chooseTarget() {
		Map<String, Object> target = webClient.post().uri("/chooseTarget").bodyValue(
				new ChooseTargetRequest().setTargetId("test2").setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", target.get("nextActions"));		
	}

	@Test
	public void a028_turn3_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("barrel;play;pass", target.get("nextActions"));		
		Assertions.assertTrue(target.get("validCards") instanceof Map);
		Assertions.assertTrue(((Map) target.get("validCards")).values().stream().anyMatch(c -> ((Map) c).get("name").equals("Missed")));
		
	}

	@Test
	public void a029_turn3_play() {
		Map<String, Object> target = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Missed"}).setCardIds(createUUIDs(new Missed("7", "Diamonds"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", target.get("nextActions"));	
	}

	@Test
	public void a030_turn3_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("play;discardRule", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a031_turn3_discardRule() {
		Map<String, Object> discard = webClient.post().uri("/discardRule").bodyValue(
				new DiscardRuleRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals(0, discard.get("numberToDiscard"));
	}

	@Test
	public void a032_turn3_discard() {
				
		Map<String, Object> discard = webClient.post().uri("/discard").bodyValue(
				new DiscardRequest().setCardNames(new String[] {}).setCardIds(new UUID[] {}).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", discard.get("nextActions"));
	}

	@Test
	public void a033_turn3_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("draw", wait4.get("nextActions"));
	}

	@Test
	public void a035_turn4_draw() {
		Map<String, Object> draw = webClient.post().uri("/draw").bodyValue(
				new DrawRequest().setSourceName("deck").setNumberToDraw(2).setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)draw.get("cards")).size());
		Assertions.assertEquals("play;discardRule", draw.get("nextActions"));
		
		state.setLastCards(((List<LinkedHashMap>) draw.get("cards")).stream().map(c -> c.get("name").toString()).collect(Collectors.toList()));
	}

	@Test
	public void a036_turn4_play() {
		Map<String, Object> generalStore = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"General Store"}).setCardIds(createUUIDs(new GeneralStore("A", "Diamonds"))).setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("chooseCard", generalStore.get("nextActions"));	
		Assertions.assertTrue(generalStore.get("cards") instanceof List);
		Assertions.assertEquals(4, ((List)generalStore.get("cards")).size());
	}

	@Test
	public void a037_turn4_general_store_draw() {
		Map<String, Object> draw = webClient.post().uri("/chooseCard").bodyValue(
				new ChooseCardRequest().setCardName("Bang").setCardId(createUUID(new Bang("8", "Diamonds"))).setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(1, ((List)draw.get("cards")).size());
		Assertions.assertEquals("wait", draw.get("nextActions"));
	}

	@Test
	public void a038_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("chooseCard", wait1.get("nextActions"));
		Assertions.assertTrue(wait1.get("cards") instanceof List);
		Assertions.assertEquals(3, ((List)wait1.get("cards")).size());
		
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}

	@Test
	public void a039_turn4_general_store_draw() {
		Map<String, Object> draw = webClient.post().uri("/chooseCard").bodyValue(
				new ChooseCardRequest().setCardName("Bang").setCardId(createUUID(new Bang("J", "Diamonds"))).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(1, ((List)draw.get("cards")).size());
		Assertions.assertEquals("wait;dismissMessage", draw.get("nextActions"));
	}

	@Test
	public void a040_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		
		Assertions.assertEquals("chooseCard", wait2.get("nextActions"));
		Assertions.assertTrue(wait2.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)wait2.get("cards")).size());
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}

	@Test
	public void a041_turn4_general_store_draw() {
		Map<String, Object> draw = webClient.post().uri("/chooseCard").bodyValue(
				new ChooseCardRequest().setCardName("Bang").setCardId(createUUID(new Bang("4", "Clubs"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(1, ((List)draw.get("cards")).size());
		Assertions.assertEquals("wait", draw.get("nextActions"));
	}

	@Test
	public void a042_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("chooseCard", wait3.get("nextActions"));
		Assertions.assertTrue(wait3.get("cards") instanceof List);
		Assertions.assertEquals(1, ((List)wait3.get("cards")).size());
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}
	
	@Test
	public void a043_turn4_general_store_draw() {
		Map<String, Object> draw = webClient.post().uri("/chooseCard").bodyValue(
				new ChooseCardRequest().setCardName("Bang").setCardId(createUUID(new Bang("7", "Diamonds"))).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(1, ((List)draw.get("cards")).size());
		Assertions.assertEquals("wait", draw.get("nextActions"));
	}

	@Test
	public void a044_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("wait;dismissMessage", wait1.get("nextActions"));
		
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("play;discardRule", wait4.get("nextActions"));
		
	}

	@Test
	public void a045_turn4_play() {
		Map<String, Object> indians = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Indians"}).setCardIds(createUUIDs(new Indians("6", "Diamonds"))).setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", indians.get("nextActions"));
	}

	@Test
	public void a046_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("defenseOptions", wait1.get("nextActions"));
		
		Assertions.assertEquals("defenseOptions", wait2.get("nextActions"));
		Assertions.assertEquals("defenseOptions", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}
	
	@Test
	public void a047_turn4_defense() {
		Map<String, Object> target1 = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> target2 = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> target3 = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("play;pass", target1.get("nextActions"));
		Assertions.assertEquals("play;pass", target2.get("nextActions"));
		Assertions.assertEquals("play;pass", target3.get("nextActions"));		
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}

	@Test
	public void a048_turn4_pass() {
		Map<String, Object> target = webClient.post().uri("/pass").bodyValue(
				new PassRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", target.get("nextActions"));		
	}
	
	@Test
	public void a049_turn4_wait() {
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}

	@Test
	public void a050_turn4_play() {
		Map<String, Object> play2 = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("4", "Clubs"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> play3 = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("7", "Diamonds"))).setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", play2.get("nextActions"));
		Assertions.assertEquals("wait", play3.get("nextActions"));
	}
	
	@Test
	public void a051_turn4_wait() {
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("play;discardRule", wait4.get("nextActions"));
		
	}

	@Test
	public void a052_turn4_discardRule() {
		Map<String, Object> discard = webClient.post().uri("/discardRule").bodyValue(
				new DiscardRuleRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		// got an extra one from the general store
		Assertions.assertEquals(1, discard.get("numberToDiscard"));
	}

	@Test
	public void a053_turn4_discard() {
				
		Map<String, Object> discard = webClient.post().uri("/discard").bodyValue(
				new DiscardRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("8", "Diamonds"))).setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", discard.get("nextActions"));
	}

	@Test
	public void a054_turn4_wait() {
		Map<String, Object> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, Object> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();

		Assertions.assertEquals("draw", wait1.get("nextActions"));
		Assertions.assertEquals("wait", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
		
	}

	@Test
	public void a056_turn5_draw() {
		Map<String, Object> draw = webClient.post().uri("/draw").bodyValue(
				new DrawRequest().setSourceName("deck").setNumberToDraw(2).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertTrue(draw.get("cards") instanceof List);
		Assertions.assertEquals(2, ((List)draw.get("cards")).size());
		Assertions.assertEquals("play;discardRule", draw.get("nextActions"));
		
		state.setLastCards(((List<LinkedHashMap>) draw.get("cards")).stream().map(c -> c.get("name").toString()).collect(Collectors.toList()));
	}
	
	@Test
	public void a057_turn5_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Duel"}).setCardIds(createUUIDs(new Duel("Q", "Hearts"))).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
				
		Assertions.assertEquals("chooseTarget", play.get("nextActions"));
		Assertions.assertTrue(play.get("targets") instanceof List);
		Assertions.assertEquals(3, ((List)play.get("targets")).size());
	}

	@Test
	public void a058_turn5_chooseTarget() {
		Map<String, Object> target = webClient.post().uri("/chooseTarget").bodyValue(
				new ChooseTargetRequest().setTargetId("test2").setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", target.get("nextActions"));		
	}

	@Test
	public void a059_turn5_wait() {
		Map<String, String> wait1 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait2 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait3 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test3")
			).retrieve().bodyToMono(Map.class).block();
		Map<String, String> wait4 = webClient.post().uri("/wait").bodyValue(
				new WaitingRequest().setGameName("test1").setPlayerId("test4")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait", wait1.get("nextActions"));
		Assertions.assertEquals("defenseOptions", wait2.get("nextActions"));
		Assertions.assertEquals("wait", wait3.get("nextActions"));
		Assertions.assertEquals("wait", wait4.get("nextActions"));
	}

	@Test
	public void a060_turn5_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;pass", target.get("nextActions"));	
		System.out.println(target.get("validCards"));
		System.out.println(createUUIDs(new Bang("4", "Diamonds"))[0].toString());
	}
	
	@Test
	public void a061_turn5_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("4", "Diamonds"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
				
		Assertions.assertEquals("wait", play.get("nextActions"));
	}


	@Test
	public void a062_turn5_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;pass", target.get("nextActions"));		
	}
	
	@Test
	public void a063_turn5_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("9", "Clubs"))).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
				
		Assertions.assertEquals("wait", play.get("nextActions"));
	}

	@Test
	public void a064_turn5_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;pass", target.get("nextActions"));		
	}
	
	@Test
	public void a065_turn5_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("5", "Diamonds"))).setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
				
		Assertions.assertEquals("wait", play.get("nextActions"));
	}


	@Test
	public void a066_turn5_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("play;pass", target.get("nextActions"));		
	}
	
	@Test
	public void a067_turn5_play() {
		Map<String, Object> play = webClient.post().uri("/play").bodyValue(
				new PlayRequest().setCardNames(new String[] {"Bang"}).setCardIds(createUUIDs(new Bang("7", "Diamonds"))).setGameName("test1").setPlayerId("test1")
			).retrieve().bodyToMono(Map.class).block();
				
		Assertions.assertEquals("wait", play.get("nextActions"));
	}

	@Test
	public void a068_turn5_defenseOptions() {
		Map<String, Object> target = webClient.post().uri("/defenseOptions").bodyValue(
				new DefenseOptionsRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("pass", target.get("nextActions"));		
	}

	@Test
	public void a069_turn5_pass() {
		Map<String, Object> target = webClient.post().uri("/pass").bodyValue(
				new PassRequest().setGameName("test1").setPlayerId("test2")
			).retrieve().bodyToMono(Map.class).block();
		
		Assertions.assertEquals("wait;dismissMessage", target.get("nextActions"));		
	}
}
