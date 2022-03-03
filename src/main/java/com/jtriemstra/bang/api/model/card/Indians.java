package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.action.MissIndians;
import com.jtriemstra.bang.api.model.action.Waiting;
import com.jtriemstra.bang.api.model.player.Player;

public class Indians extends Card {
	
	public Indians(String denomination, String suit) {
		super(denomination, suit, "Indians");
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		for (Player p : game.getPlayers().getArray()) {
			if (p != player) {
				List<DefenseRule> defenseRules = new ArrayList<>();
				defenseRules.add(new DefenseRule(DefenseRule.TYPE_MISSED, (defender, attacker1, attackCard) -> defender.countCardsInHand("Bang") > 0, actions -> actions.add(new MissIndians(player))));
				
				DefenseOptions defense = new DefenseOptions(player, defenseRules, this);
				p.addNextAction(defense);

				player.addNextAction(new Waiting());
			}
		}
		
		return new PlayResponse();
	}

}
