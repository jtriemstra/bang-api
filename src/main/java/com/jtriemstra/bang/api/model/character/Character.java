package com.jtriemstra.bang.api.model.character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.action.ActionList;
import com.jtriemstra.bang.api.model.action.BaseAction;
import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.action.DiscardRule;
import com.jtriemstra.bang.api.model.action.Draw;
import com.jtriemstra.bang.api.model.action.Miss;
import com.jtriemstra.bang.api.model.action.Play;
import com.jtriemstra.bang.api.model.action.PossibleActions;
import com.jtriemstra.bang.api.model.action.Waiting;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Playable;
import com.jtriemstra.bang.api.model.player.BangRule;
import com.jtriemstra.bang.api.model.player.BangRuleDefault;
import com.jtriemstra.bang.api.model.player.Hand;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.Data;
import lombok.Getter;

@Data
public class Character {
	@Getter
	private String name;
	private int initialHealth;
	
	public Character(String name, int initialHealth) {
		this.name = name;
		this.initialHealth = initialHealth;		
	}
	
	public int getRangeWhenOnOffense() {
		return 0;
	}
	
	public int getRangeWhenOnDefense() {
		return 0;
	}
	
	public void alterDefenseRules(List<DefenseRule> rules, Player attackingPlayer) {
		
	}
	
	public Miss createMissAction(Player thisPlayer, Card attackCard) {
		return new Miss(thisPlayer);
	}
	
	public void doHit(Player attacker, Player defender) {
		
	}
	
	public Hand createHand(Player p) {
		return new Hand();
	}
		
	public ActionList createInitialActions(ActionList in) {
		in.push(new Play(), new DiscardRule());
		in.push(new Draw(2, "deck"));
		return in;
	}

	public BangRule createBangRule(Player player) {
		return new BangRuleDefault(player);
	}
	
	public List<BiFunction<Player, Card, Boolean>> getPlayInvalidRules() {
		return List.of(
			(p, c) -> c.getName().equals("Beer") && p.getCurrentHealth() == p.getMaxHealth(),
			(p, c) -> c.getName().equals("Missed"),
			(p, c) -> c.getTable() && p.getTable().hasCard(c.getName()),
			(p, c) -> c.getName().equals("Bang") && !p.getBangRule().isValid()
		);
	}
	
	public Predicate<Card> getMissValidFilter(Miss missAction) {
		return c -> missAction.getValidCardsToDefend().contains(c.getName());
	}
	
	public Playable getMissPlayable(Card c, Miss missAction) {
		return c;
	}
	
	public Playable getPlayableFromCard(Card c, Play action) {
		return c;
	}

	public void addActions(Player p, ActionList actions) {
		
	}

	public void init(Player player, Game game) {
		
	}
}
