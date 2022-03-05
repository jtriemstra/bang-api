package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.player.BangRule;
import com.jtriemstra.bang.api.model.player.BangRuleWillyTheKid;
import com.jtriemstra.bang.api.model.player.Player;

public class WillyTheKid extends Character {
	public WillyTheKid() {
		super("Willy the Kid", 4);
	}

	@Override
	public BangRule createBangRule(Player p) {
		return new BangRuleWillyTheKid();
	}
}
