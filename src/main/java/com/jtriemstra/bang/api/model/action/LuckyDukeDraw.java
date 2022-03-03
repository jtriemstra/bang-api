package com.jtriemstra.bang.api.model.action;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class LuckyDukeDraw extends BaseAction {
	
	private BaseAction innerAction;
	
	public LuckyDukeDraw(BaseAction innerAction) {
		this.innerAction = innerAction;
	}

	@Override
	public String getName() {
		return innerAction.getName();
	}

	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {

		List<Card> revealedCards = new ArrayList<>();
		
		for (int i=0; i<2; i++) {
			Card cardToShow = null;
			cardToShow = game.draw();
			
			revealedCards.add(cardToShow);
		}
		
		//player.popAction();
		player.addNextAction(new LuckyDukeCardChoice(innerAction, revealedCards));
		
		return new DrawResponse(revealedCards);
	}

}
