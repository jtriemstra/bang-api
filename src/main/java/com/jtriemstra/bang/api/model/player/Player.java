package com.jtriemstra.bang.api.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;

import com.jtriemstra.bang.api.action.ActionList;
import com.jtriemstra.bang.api.action.BaseAction;
import com.jtriemstra.bang.api.action.DefenseRule;
import com.jtriemstra.bang.api.action.DismissMessage;
import com.jtriemstra.bang.api.action.Miss;
import com.jtriemstra.bang.api.action.Play;
import com.jtriemstra.bang.api.action.PossibleActions;
import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.GameState;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Gun;
import com.jtriemstra.bang.api.model.card.Playable;
import com.jtriemstra.bang.api.model.character.Character;

import lombok.Getter;

public class Player {
	private ActionList actions;
	private Game game;
	@Getter
	private Character character;
	@Getter
	private Role role;
	@Getter
	private Hand hand;
	@Getter
	private int currentHealth;
	@Getter
	private int maxHealth;
	private String name;
	@Getter
	private Table table;
	@Getter
	private List<String> messages;
	@Getter
	private BangRule bangRule;
	
	public Player(String name, Role role, Character character, Game game) {
		this.name = name;
		this.role = role;
		this.character = character;
		this.actions = new ActionList();
		this.hand = character.createHand(this);
		this.table = new Table();
		this.currentHealth = this.maxHealth = character.getInitialHealth() + (role == Role.SHERIFF ? 1 : 0);
		this.game = game;
		this.bangRule = character.getBangRule(this);
		actions.push(character.getPermanentWait());
		this.messages = new ArrayList<>();
	}
	
	public void startTurn() {
		actions = character.createInitialActions(actions);
		bangRule.reset();
	}
	
	public BaseResponse doAction(BaseRequest request) {
		BaseAction a = actions.getCurrentByName(request.getActionName());
		BaseResponse response = a.execute(request, this, game);
		character.addActions(this, actions);
		response.setNextActions(actions.getNext());
		response.setState(new GameState(game, this));
		Map<UUID, Playable> validCards = getValidCards(actions.getNext(), hand.getCards(), this); 
		response.setValidCards(validCards);
		if (actions.getNext().contains("play")) {
			((Play) actions.getNext().getByName("play")).setValidCardIds(validCards.keySet());
		}
		
		
		return response;
	}
	
	public int getActionCount() {
		return actions.size();
	}
	
	public Map<UUID, Playable> getValidCards(PossibleActions nextActions, List<Card> cards, Player p) {
		if (nextActions.contains("play")) {
			if (nextActions.getByName("play") instanceof Miss) {
				Map<UUID, Playable> results = new HashMap<>();
				Miss missAction = (Miss) nextActions.getByName("play");
				p.getHand().getCards()
					.stream()
					.filter(character.getMissValidFilter(missAction))
					.forEach(c -> results.put(c.getId(), character.getMissPlayable(c, missAction)));
				
				return results;
			}
			else {
				Map<UUID, Playable> results = new HashMap<>();
				List<BiFunction<Player, Card, Boolean>> invalidRules = character.getPlayInvalidRules();
				
				for (Card c : p.getHand().getCards()) {
					if (!invalidRules.stream().anyMatch(r -> r.apply(p, c))) {
						results.put(c.getId(), c);				
					}
				}
				return results;
			}
		}
		return new HashMap<>();
	}

	public int getRangeWhenOnOffense(int gunAddition) {
		int range = gunAddition;
		range += character.getRangeWhenOnOffense();
		if (table.hasCard("Scope")) {
			range += 1;
		}
		
		return range;
	}
	
	public int getRangeWhenOnDefense(int startingRange) {
		int range = startingRange;
		range += character.getRangeWhenOnDefense();
		if (table.hasCard("Mustang")) {
			range += 1;
		}
		
		return range;
	}
	
	public Card gain(Card draw) {
		return hand.add(draw);
	}
	
	public void draw() {
		gain(this.game.draw());
	}
	
	public Card getCardByName(String name) {
		return hand.getByName(name);
	}
	
	public int countCardsInHand(String... names) {
		return hand.countByNames(names);
	}
	
	public boolean hasCardOnTable(String name) {
		return table.hasCard(name);
	}

	public int getHandSize() {
		return hand.getSize();
	}

	public int getHealth() {
		return currentHealth;
	}
	
	public void discard(Card c) {
		hand.remove(c);		
	}
	
	public Card loseCardFromHand() {
		Random r = new Random();
		Card c = hand.remove(r.nextInt(hand.getSize()));
		return c;
	}

	public void addHealth() {
		if (maxHealth > currentHealth) {
			currentHealth++;
		}
		else {
			throw new RuntimeException("player is already at max health");
		}
	}
	
	public boolean isBelowMaxHealth() {
		return maxHealth > currentHealth;
	}

	public String getName() {
		return name;
	}

	public List<Player> getTargets(int range) {
		return game.getPlayersInRange(this, range);
	}

	public void tryBang() {
		bangRule.execute();
	}

	public void addNextAction(BaseAction... a) {
		actions.push(a);
	}

	public PossibleActions popAction() {
		return actions.pop();
	}
		
	public void unwait() {
		popAction();
	}

	public boolean loseHealthAndCheckEndGame(Player attackingPlayer) {
		currentHealth--;
		character.doHit(attackingPlayer, this);

		if (currentHealth == 0) {
			if (hand.cardInHand("Beer")) {
				addMessage("Your health went to zero, and a Beer was played");
				
				PlayRequest playRequest = new PlayRequest().setCardNames(new String[] {"Beer"}).setCardIds(new UUID[] {hand.getByName("Beer").getId()});
				Play playAction = new Play(new String[] {"Beer"});
				playAction.execute(playRequest, this, game);
			}
			else { 
				return die(attackingPlayer);
			}
		}
		else {
			this.addMessage("Your health is now " + this.currentHealth);
		}
		
		return false;
	}
	
	private boolean die(Player attackingPlayer) {
		addMessage("You have died");
		game.notify("Player " + name + " has died. They had a role of " + role);
		
		if (attackingPlayer.getRole() == Role.SHERIFF && this.role == Role.DEPUTY) {
			attackingPlayer.getHand().clear();
			attackingPlayer.getTable().clear();
		}
		else if (this.role == Role.OUTLAW) {
			attackingPlayer.draw();
			attackingPlayer.draw();
			attackingPlayer.draw();
		}				
		
		return game.killPlayer(this, attackingPlayer);
	}
		
	public Gun getGun() {
		return table.getGun();
	}
	
	public void discardFromTable(Card c) {
		table.remove(c);
		game.discard(c);
	}
	
	public void playToTable(Card c) {
		table.add(c);
	}
	
	public void removeActionOption(String name) {
		actions.getNext().removeAction(name);
	}
	
	public Miss getMissAction(Card attackCard) {
		return character.getMissAction(this, attackCard);
	}
	
	public void addMessage(String string) {
		messages.add(string);
		if (!actions.getNext().toString().contains("dismissMessage")) {
			actions.getNext().addAction(new DismissMessage());
		}
	}

	public Card getCardById(UUID id) {
		return hand.getById(id);
	}
	
	public Playable getPlayableById(UUID id, Play action) {
		return character.getPlayableFromCard(getCardById(id), action);
	}
	
	public void alterDefenseRules(List<DefenseRule> rules, Player attackingPlayer) {
		character.alterDefenseRules(rules, attackingPlayer);
	}
}
