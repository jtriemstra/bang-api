package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.action.DefenseOptions;
import com.jtriemstra.bang.api.model.player.Player;

public class BartCassidy extends Character {
	public BartCassidy() {
		super("Bart Cassidy", 4);
	}
	
	@Override
	public void doHit(Player attacker, Player defender) {
		defender.draw();
	}
}
