package com.jtriemstra.bang.api.model.character;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.action.Miss;
import com.jtriemstra.bang.api.model.action.Play;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.JanetCardWrapper;
import com.jtriemstra.bang.api.model.card.Playable;
import com.jtriemstra.bang.api.model.player.Player;

public class CalamityJanet extends Character {
	public CalamityJanet() {
		super("Calamity Janet", 4);
	}
	
	@Override
	public void alterDefenseRules(List<DefenseRule> rules, Player attackingPlayer) {
		rules
		.stream()
		.filter(r -> r.getName().equals(DefenseRule.TYPE_MISSED))
		.forEach(r -> r.setCondition(
			(defender, attacker, attackCard) -> {
				Miss m = attacker.getMissAction(attackCard);
				return defender.countCardsInHand("Missed", "Bang") >= m.getNumberOfCardsToPlay();
			}
		));
	}
	
	@Override
	public List<BiFunction<Player, Card, Boolean>> getPlayInvalidRules() {
		return List.of(
			(p, c) -> c.getName().equals("Beer") && p.getCurrentHealth() == p.getMaxHealth(),
			(p, c) -> c.getTable() && p.getTable().hasCard(c.getName()),
			(p, c) -> (c.getName().equals("Bang") || c.getName().equals("Missed")) && !p.getBangRule().isValid()
		);
	}
	

	@Override
	public Predicate<Card> getMissValidFilter(Miss missAction) {
		return c -> {
			return missAction.getValidCardsToDefend().contains(c.getName())
					|| (c.getName().equals("Bang") && missAction.getValidCardsToDefend().contains("Missed"))
					|| (c.getName().equals("Missed") && missAction.getValidCardsToDefend().contains("Bang"));
		};
	}
	
	@Override
	public Playable getMissPlayable(Card c, Miss missAction) {
		return new JanetCardWrapper(c, 
				missAction.getValidCardsToDefend().contains("Bang"), 
				missAction.getValidCardsToDefend().contains("Missed"));
	}

	@Override
	public Playable getPlayableFromCard(Card c, Play action) {
		return new JanetCardWrapper(c, action instanceof Play && !(action instanceof Miss), action instanceof Miss);
	}
}
