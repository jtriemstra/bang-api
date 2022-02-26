package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.action.BaseAction;
import com.jtriemstra.bang.api.dto.response.PlayResponse;

public class WellsFargo extends Card {

	public WellsFargo(String denomination, String suit) {
		super(denomination, suit, "Wells Fargo");
	}
	
	@Override
	public PlayResponse play(Player p, Game game) {
		for (int i=0; i<3; i++) {
			Card c = game.draw();
			p.gain(c);	
		}
		
		return new PlayResponse();
	}

}
