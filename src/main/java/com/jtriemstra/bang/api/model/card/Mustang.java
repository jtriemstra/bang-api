package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Mustang extends Card {

	public Mustang(String denomination, String suit) {
		super(denomination, suit, "Mustang");
	}
	
	@Override
	public PlayResponse play(Player p, Game game) {
		p.playToTable(this);
		return new PlayResponse();
	}
}
