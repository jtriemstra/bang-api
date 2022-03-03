package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.response.BarrelResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class Jourdonnais extends BaseAction {
	private Player attackingPlayer;
	
	public Jourdonnais(Player attackingPlayer) {
		this.attackingPlayer = attackingPlayer;
	}
	
	@Override
	public String getName() {
		return "jourdonnais";
	}

	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		Card c = game.draw();
		game.discard(c);
		
		if (c.getSuit().equals("Hearts")) {
			attackingPlayer.unwait();
			player.popAction();
		}
		else {
			player.removeActionOption("jourdonnais");
		}
		
		return new BarrelResponse();
	}

}
