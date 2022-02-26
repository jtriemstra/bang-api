package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.action.Barrel;
import com.jtriemstra.bang.api.action.ChooseTarget;
import com.jtriemstra.bang.api.action.DefenseOptions;
import com.jtriemstra.bang.api.action.DefenseRule;
import com.jtriemstra.bang.api.action.Miss;
import com.jtriemstra.bang.api.action.Waiting;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;


public class Bang extends Card implements TargetingCard {

	public Bang(String denomination, String suit) {
		super(denomination, suit, "Bang");
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		player.tryBang();

		int gunRange = player.getGun() == null ? 1 : player.getGun().getRange();
		List<Player> targets = player.getTargets(gunRange);
		if (targets.size() == 0) {
			throw new RuntimeException("there is nobody within range");			
		}
		
		List<String> targetNames = new ArrayList<String>();
		for (Player p1 : targets) {
			targetNames.add(p1.getName());
		}
		
		player.addNextAction(new ChooseTarget(this, targets));
		
		return new TargetingCardResponse(targetNames);
	}

	@Override
	public BaseResponse doAttack(Player attacker1, Player defender1, Game game) {
		List<DefenseRule> defenseRules = new ArrayList<>();
		defenseRules.add(new DefenseRule(DefenseRule.TYPE_BARREL, (defender, attacker, attackCard) -> defender.hasCardOnTable("Barrel"), actions -> actions.add(new Barrel(attacker1))));
		Miss missAction = attacker1.getMissAction(this);
		defenseRules.add(new DefenseRule(DefenseRule.TYPE_MISSED, (defender, attacker, attackCard) -> defender.countCardsInHand("Missed") >= missAction.getNumberOfCardsToPlay(), actions -> actions.add(missAction)));
		
		DefenseOptions defense = new DefenseOptions(attacker1, defenseRules, this);
				
		defender1.addNextAction(defense);

		attacker1.addNextAction(new Waiting());
		
		return new WaitingResponse();
	}

}
