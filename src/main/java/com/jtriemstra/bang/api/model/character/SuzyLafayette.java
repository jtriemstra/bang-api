package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.player.Hand;
import com.jtriemstra.bang.api.model.player.HandSuzyLafayette;
import com.jtriemstra.bang.api.model.player.Player;

public class SuzyLafayette extends Character {
	public SuzyLafayette() {
		super("Suzy Lafayette", 4);
	}
	
	@Override
	public Hand createHand(Player p) {
		return new HandSuzyLafayette(p);
	}
}
