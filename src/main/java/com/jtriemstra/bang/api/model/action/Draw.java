package com.jtriemstra.bang.api.model.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.DrawRequest;
import com.jtriemstra.bang.api.dto.request.DrawSourceRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class Draw extends BaseAction {
	protected int numberToDraw;
	protected String[] validDrawSources;
	protected String currentDrawSource;
	
	public Draw() {
		this.numberToDraw = 2;
		this.validDrawSources = new String[] {"deck"};
		this.currentDrawSource = "deck";
	}
	
	public Draw(int numberToDraw, String[] validDrawSources) {
		this.numberToDraw = numberToDraw;
		this.validDrawSources = validDrawSources;
	}
	
	public Draw(int numberToDraw, String currentSource) {
		this.numberToDraw = numberToDraw;
		this.currentDrawSource = currentSource;
	}
	
	@Override
	public String getName() {
		return "draw";
	}
	
	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		DrawRequest drawRequest = (DrawRequest) request;
		
		if (currentDrawSource == null) {
			if (!Arrays.stream(validDrawSources).anyMatch(drawRequest.getSourceName()::equals)) {
				throw new RuntimeException("you cannot draw from " + drawRequest.getSourceName() + " at this time");
			}			
			currentDrawSource = drawRequest.getSourceName();
		}
				
		
		if (drawRequest.getNumberToDraw() > numberToDraw) {
			throw new RuntimeException("you can only draw " + numberToDraw + " cards");
		}
		
		game.notify("Player " + player.getName() + " draws " + numberToDraw + " from " + currentDrawSource);
		
		List<Card> gainedCards = new ArrayList<>();
		
		boolean drawSucceeds = true;
		
		for (int i=0; i<numberToDraw; i++) {
			Card cardToGain = null;

			if (this.currentDrawSource.equals("discard")) {
				cardToGain = game.getDiscard().removeTop();
			}
			else if (this.currentDrawSource.equals("deck")){
				cardToGain = game.draw();
			}
			else {
				Player drawSource = game.getPlayerById(this.currentDrawSource);
				if (drawSource.getHandSize() > 0) {
					cardToGain = drawSource.loseCardFromHand();
				}
				else {
					player.addMessage("This player has no cards to draw from");
					drawSucceeds = false;
				}
			}
			gainedCards.add(player.gain(cardToGain));
		}
		
		if (drawSucceeds) {
			player.popAction();
			
			return new DrawResponse(gainedCards);	
		}
		else {
			return new DrawSourceResponse(1, validDrawSources);
		}
		
		
	}
	
}
