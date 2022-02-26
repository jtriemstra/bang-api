package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.action.ChooseTarget;
import com.jtriemstra.bang.api.action.DefenseOptions;
import com.jtriemstra.bang.api.action.DefenseRule;
import com.jtriemstra.bang.api.action.MissDuel;
import com.jtriemstra.bang.api.action.Waiting;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Duel extends Card implements TargetingCard {
	
	public Duel(String denomination, String suit) {
		super(denomination, suit, "Duel");
	}

	@Override
	public PlayResponse play(Player p, Game game) {
		List<Player> result = new ArrayList<Player>();
		for (Player p1 : game.getPlayers().getArray()) {
			if (p1 != p) {
				result.add(p1);
			}
		}
		
		List<String> targetNames = new ArrayList<String>();
		for (Player p1 : result) {
			targetNames.add(p1.getName());
		}
		
		p.addNextAction(new ChooseTarget(this, result));
		
		return new TargetingCardResponse(targetNames);
	}

	@Override
	public BaseResponse doAttack(Player attacker, Player defender1, Game game) {
		List<DefenseRule> defenseRules = new ArrayList<>();
		defenseRules.add(new DefenseRule(DefenseRule.TYPE_MISSED, (defender, attacker1, attackCard) -> defender.countCardsInHand("Bang") > 0, actions -> actions.add(new MissDuel(attacker))));
		
		DefenseOptions defense = new DefenseOptions(attacker, defenseRules, this);

		defender1.addNextAction(defense);
		
		attacker.addNextAction(new Waiting());
		
		return new WaitingResponse();
	}

}
