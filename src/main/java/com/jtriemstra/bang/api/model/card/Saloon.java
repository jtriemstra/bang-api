package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Saloon extends Card {

	public Saloon(String denomination, String suit) {
		super(denomination, suit, "Saloon");
	}
	
	@Override
	public PlayResponse play(Player thisPlayer, Game game) {
		for (Player p : game.getPlayers().getArray()) {
			if (p.isBelowMaxHealth()) {
				p.addHealth();
				p.addMessage("The Saloon added one health");
			}
		}

		return new PlayResponse();
	}

}
