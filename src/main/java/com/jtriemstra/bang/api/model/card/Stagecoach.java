package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.action.BaseAction;
import com.jtriemstra.bang.api.dto.response.PlayResponse;

public class Stagecoach extends Card {

	public Stagecoach(String denomination, String suit) {
		super(denomination, suit, "Stagecoach");
	}
	
	@Override
	public PlayResponse play(Player p, Game game) {
		for (int i=0; i<2; i++) {
			Card c = game.draw();
			p.gain(c);	
		}
		
		return new PlayResponse();
	}

}
