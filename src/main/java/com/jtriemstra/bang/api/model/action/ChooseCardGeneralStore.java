package com.jtriemstra.bang.api.model.action;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.ChooseCardRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.Getter;
import lombok.Setter;

public class ChooseCardGeneralStore extends BaseAction {
	List<Card> cards;
	@Getter
	String currentPlayerName;
	
	public ChooseCardGeneralStore(List<Card> cards) {
		this.cards = cards;
	}
	
	@Override
	public String getName() {
		return "chooseCard";
	}
	
	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		ChooseCardRequest drawRequest = (ChooseCardRequest) request;
		
		List<Card> gainedCards = new ArrayList<>();
		
		Card cardToGain = null;
		for (Card c : cards) {
			if (c.getId().equals(drawRequest.getCardId())) {
				cardToGain = c;
				break;
			}
		}
		
		if (cardToGain == null) {
			throw new RuntimeException("requested card was not found");
		}
		
		game.notify("Player " + player.getName() + " takes " + drawRequest.getCardName());
		
		player.gain(cardToGain);
		cards.remove(cardToGain);
		gainedCards.add(cardToGain);
		
		player.popAction();
		if (cards.size() > 0) {
			game.getNextPlayer(player).popAction();
			game.getNextPlayer(player).addNextAction(new WaitingGeneralStore(this));
		}
		else {
			game.getNextPlayer(player).popAction();
		}
		
		
		return new DrawResponse(gainedCards);
	}
}
