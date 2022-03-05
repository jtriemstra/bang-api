package com.jtriemstra.bang.api.model.action;

import java.util.UUID;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.request.DiscardRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DiscardResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Discard extends BaseAction {
	
	private int discardCount;

	@Override
	public String getName() {
		return "discard";
	}
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		DiscardRequest discardRequest = (DiscardRequest) request;
		
		game.notify("Player " + player.getName() + " discards " + discardRequest.getCardNames().length + " cards");
		
		if (discardCount != discardRequest.getCardNames().length) {
			throw new RuntimeException("You must discard " + discardCount+ " cards");
		}
		
		for (UUID s : discardRequest.getCardIds()) {
			Card c = player.getCardById(s);
			player.discard(c);
			game.discard(c);
		}
	
		player.popAction();
		game.moveToNextPlayer();
		
		return new DiscardResponse();
	}
}
