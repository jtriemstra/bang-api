package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.action.BaseAction;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.dto.response.PlayResponse;

public class Gun extends Card {

	public Gun(String denomination, String suit, String name) {
		super(denomination, suit, name);
	}
	
	public int getRange() {
		return 1;
	}

	@Override
	public boolean getTable() {
		return true;
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		Gun oldGun = player.getGun();
		if (oldGun != null) {
			player.discardFromTable(oldGun);
		}
		player.playToTable(this);
		return new PlayResponse();
	}

	
}
