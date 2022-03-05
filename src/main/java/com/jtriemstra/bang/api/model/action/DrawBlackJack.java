package com.jtriemstra.bang.api.model.action;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class DrawBlackJack extends Draw {

	public DrawBlackJack() {
		super();
	}
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		
		List<Card> gainedCards = new ArrayList<>();
		
		for (int i=0; i<numberToDraw; i++) {
			Card cardToGain = game.draw();
						
			gainedCards.add(player.gain(cardToGain));
			
			if (i == 1 && (cardToGain.getSuit().equals("Hearts") || cardToGain.getSuit().equals("Diamonds"))) {
				game.notify(player.getName() + " drew " + cardToGain.getName() + " with a Heart or Diamond, and gets a third card");
				gainedCards.add(player.gain(game.draw()));
			}
		}
		
		player.popAction();
		
		return new DrawResponse(gainedCards);
	}
}
