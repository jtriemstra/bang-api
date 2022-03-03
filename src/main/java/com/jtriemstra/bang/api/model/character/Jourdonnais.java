package com.jtriemstra.bang.api.model.character;

import java.util.List;

import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.player.Player;

public class Jourdonnais extends Character {
	public Jourdonnais() {
		super("Jourdonnais", 4);
	}
	
	@Override
	public void alterDefenseRules(List<DefenseRule> rules, Player attackingPlayer) {
		rules.add(new DefenseRule("jourd", (defender, attacker, attackCard) -> true, actions -> actions.add(new com.jtriemstra.bang.api.model.action.Jourdonnais(attackingPlayer))));
	}
}
