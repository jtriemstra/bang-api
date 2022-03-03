package com.jtriemstra.bang.api.model.action;

import java.util.UUID;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class DiscardSidKetchum extends BaseAction {

	@Override
	public String getName() {
		return "discard";
	}
	
	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		DiscardRequest sidRequest = (DiscardRequest) request;
		
		game.notify(player.getName() + " discards 2 cards to gain one health");
				
		for (UUID s : sidRequest.getCardIds()) {
			Card c = player.getCardById(s);
			player.discard(c);
			game.discard(c);
		}

		player.addHealth();
		player.popAction();
		//player.removeActionOption("discardRuleSid");
		
		return new BaseResponse();
	}

}
