package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;


public class Scope extends Card {

	public Scope(String denomination, String suit) {
		super(denomination, suit, "Scope");
	}
	
	@Override
	public PlayResponse play(Player p, Game game) {
		p.playToTable(this);
		return new PlayResponse();
	}

}
