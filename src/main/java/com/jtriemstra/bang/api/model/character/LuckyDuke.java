package com.jtriemstra.bang.api.model.character;

import java.util.List;

import com.jtriemstra.bang.api.model.action.Barrel;
import com.jtriemstra.bang.api.model.action.DefenseRule;
import com.jtriemstra.bang.api.model.action.LuckyDukeDraw;
import com.jtriemstra.bang.api.model.player.Player;

public class LuckyDuke extends Character {

	public LuckyDuke() {
		super("Lucky Duke", 4);
	}

	@Override
	public void alterDefenseRules(List<DefenseRule> rules, Player attackingPlayer) {
		rules.stream().filter(r -> r.getName().equals("barrel")).forEach(r -> r.setBehavior(actions -> actions.add(new LuckyDukeDraw(new Barrel(attackingPlayer)))));
	}
	
}
