package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Volcanic extends Gun {
	public Volcanic(String denomination, String suit) {
		super(denomination, suit, "Volcanic");
	}
	
	@Override
	public int getRange() {
		return 1;
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		PlayResponse r = super.play(player, game);
		return r;
	}
}
