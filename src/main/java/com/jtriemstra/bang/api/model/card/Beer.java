package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Beer extends Card {

	public Beer(String denomination, String suit) {
		super(denomination, suit, "Beer");
	}
		
	@Override
	public PlayResponse play(Player player, Game game) {
		if (player.getCurrentHealth() == player.getMaxHealth()) {
			throw new RuntimeException("cannot gain health beyond " + player.getMaxHealth());
		}
		player.addHealth();
		return new PlayResponse();
	}

}
