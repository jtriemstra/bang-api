package com.jtriemstra.bang.api.action;

import com.jtriemstra.bang.api.model.player.Player;

public class MissSlab extends Miss {

	public MissSlab(Player attackingPlayer) {
		super(attackingPlayer);
	}
	
	@Override
	public int getNumberOfCardsToPlay() {
		return 2;
	}
	
}
