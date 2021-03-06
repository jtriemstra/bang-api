package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.jtriemstra.bang.api.model.action.Quit;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.deck.CardDeck;
import com.jtriemstra.bang.api.model.deck.CharacterDeck;
import com.jtriemstra.bang.api.model.deck.RoleDeck;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.player.PlayerFactory;

import lombok.Getter;

public class Game {
	private List<String> playerNames;
	private PlayerList players;
	private CardDeck deck;
	private CharacterDeck characters;
	private RoleDeck roles;
	private DiscardPile discard;
	private String name;
	private Player currentPlayer;
	private NotificationService notifications;
	private PlayerFactory playerFactory;
	@Getter
	private String id;
	private Map<String, List<Consumer<Player>>> playerEvents;
		
	public Game(String name, String id, CharacterDeck characters, CardDeck cards, NotificationService notifications, PlayerFactory playerFactory, RoleDeck roleDeck) {
		this.name = name;
		this.characters = characters;
		this.deck = cards;
		this.roles = roleDeck;
		this.playerNames = new ArrayList<>();
		this.players = new PlayerList();
		this.discard = new DiscardPile();
		this.notifications = notifications;
		this.playerFactory = playerFactory;
		this.id = id;
		this.playerEvents = new HashMap<>();
	}
	
	public PlayerList getPlayers() {return players;}
	public DiscardPile getDiscard() {return discard;}
	
	public void addPlayer(String name) {
		playerNames.add(name);
	}
	
	public Player getNextPlayer(Player p) {
		return players.getNextPlayer(p);
	}

	public void discard(Card c) {
		discard.add(c);
	}
	
	public void returnToDeck(Card c) {
		deck.addToTop(c);
	}
	
	public Card draw() {
		if (deck.getSize() == 0) {
			List<Card> cards = discard.clearAll();
			deck.addAll(cards);
		}
		return deck.draw();
	}
	
	public List<Player> getPlayersInRange(Player currentPlayer, int startingRange){
		List<Player> targets = new ArrayList<>();
		int currentIndex = players.getIndex(currentPlayer);
		
		for (int i=0; i<players.size(); i++) {
			if (i != currentIndex) {
				int startingDistance = Math.min(Math.abs(i-currentIndex), players.size() - Math.abs(i-currentIndex));
				int offenseRange = currentPlayer.getRangeWhenOnOffense(startingRange);
				if (offenseRange >= players.getByIndex(i).getRangeWhenOnDefense(startingDistance)) {
					targets.add(players.getByIndex(i));
				}
			}
		}
		
		return targets;
	}

	public Player getPlayerById(String playerId) {
		return players.getById(playerId);
	}
	
	public void start() {
		this.roles.initialize(playerNames.size());
		
		for (String name : playerNames) {
			players.add(playerFactory.create(name, this.roles.draw(), this.characters.draw(), this));		
		}
		
		for (Player p : players.getArray()) {
			for (int i=0; i<p.getHealth(); i++) {
				Card c = deck.draw();
				p.gain(c);	
			}			
		}
		
		currentPlayer = players.getByIndex(0);
		currentPlayer.startTurn();
	}
	
	public void moveToNextPlayer() {
		for (int i=0; i<players.size(); i++) {
			if (currentPlayer == players.getByIndex(i)) {
				if (i == players.size() - 1) {
					currentPlayer = players.getByIndex(0);
				}
				else {
					currentPlayer = players.getByIndex(i+1);
				}
				
				this.notify("It is " + currentPlayer.getName() + "'s turn");
				
				currentPlayer.startTurn();
				return;
			}
		}
	}
	
	public void notify(String s) {
		notifications.notify(s);
	}
	
	public boolean killPlayer(Player p, Player attacker) {
		raiseEvent("death", p);
		
		players.remove(p);
		switch (attacker.getRole()) {
		case SHERIFF:
		case DEPUTY:
			if (!Arrays.stream(players.getArray()).anyMatch(p1 -> p1.getRole() == Role.RENEGADE || p1.getRole() == Role.OUTLAW)) {
				Arrays.stream(players.getArray()).forEach(p1 -> {
					p1.addMessage("You win");
					p1.addNextAction(new Quit());
				});
				return true;
			}
			break;
		case RENEGADE:
			if (players.size() == 1) {
				attacker.addMessage("You win");
				attacker.addNextAction(new Quit());
				return true;
			}
			break;
		case OUTLAW:
			if (p.getRole() == Role.SHERIFF) {
				Arrays.stream(players.getArray()).forEach(p1 -> {
					if (p1.getRole() == Role.OUTLAW) {
						p1.addMessage("You win");
						p1.addNextAction(new Quit());
					} else {
						p1.addMessage("You lose");
						p1.addNextAction(new Quit());
					}
				});
				return true;
			}
			break;
		}
		return false;
	}

	public void addPlayerEventListener(String eventType, Consumer<Player> handler) {
		if (!playerEvents.containsKey(eventType)) {
			playerEvents.put(eventType, new ArrayList<>());
		}
		playerEvents.get(eventType).add(handler);
	}
	
	public void raiseEvent(String eventType, Player p) {
		if (playerEvents.containsKey(eventType)) {
			playerEvents.get(eventType).forEach(h -> h.accept(p));	
		}
	}
}
