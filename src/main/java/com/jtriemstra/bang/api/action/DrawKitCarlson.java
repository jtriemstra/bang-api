package com.jtriemstra.bang.api.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.ChooseCardRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.ChooseCardResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class DrawKitCarlson extends BaseAction {

	List<Card> revealedCards;
	
	@Override
	public String getName() {
		return (revealedCards == null ? "draw" : "chooseCard");
	}

	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		if (revealedCards == null) {
			revealedCards = new ArrayList<>();
			
			for (int i=0; i<3; i++) {
				revealedCards.add(game.draw());
			}
						
			return new ChooseCardResponse(revealedCards, 2);	
		}
		else {
			ChooseCardRequest kitRequest = (ChooseCardRequest) request; 
			
			if (kitRequest.getCardNames().length != 2) {
				throw new RuntimeException("You can only choose 2 cards");
			}
			
			for (UUID chosenId : kitRequest.getCardIds()) {
				if (!revealedCards.stream().anyMatch(c -> c.getId().equals(chosenId))) {
					throw new RuntimeException("Card " + chosenId.toString() + " was not available");
				}
			}
			
			for (UUID chosenId : kitRequest.getCardIds()) {
				Card cardToGain = null;
				for (Card revealedCard : revealedCards) {
					if (chosenId.equals(revealedCard.getId())) {
						cardToGain = revealedCard;
						break;
					}
				}
				player.gain(cardToGain);
				revealedCards.remove(cardToGain);
			}
			
			for (Card c : revealedCards) {
				game.returnToDeck(c);
			}
			
			player.popAction();
			revealedCards = null;
			
			return new BaseResponse();
		}
	}

}
