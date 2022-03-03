package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.action.Barrel;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.action.Miss;
import com.jtriemstra.bang.api.model.action.Waiting;
import com.jtriemstra.bang.api.model.player.Player;

public class Gatling extends Card {

	public Gatling(String denomination, String suit) {
		super(denomination, suit, "Gatling");
	}
	
	@Override
	public PlayResponse play(Player thisPlayer, Game game) {
		for (Player p : game.getPlayers().getArray()) {
			if (p != thisPlayer) {
				List<DefenseRule> defenseRules = new ArrayList<>();
				defenseRules.add(new DefenseRule(DefenseRule.TYPE_BARREL, (defender, attacker, attackCard) -> defender.hasCardOnTable("Barrel"), actions -> actions.add(new Barrel(thisPlayer))));
				defenseRules.add(new DefenseRule(DefenseRule.TYPE_MISSED, (defender, attacker, attackCard) -> defender.countCardsInHand("Missed") > 0, actions -> actions.add(new Miss(thisPlayer))));
				
				DefenseOptions defense = new DefenseOptions(thisPlayer, defenseRules, this);

				p.addNextAction(defense);
				thisPlayer.addNextAction(new Waiting());
			}
		}
		
		return new PlayResponse();
	}

}
