package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DrawSource extends BaseAction {

	private int cardsToDraw = 2;
	private String[] validDrawSources = new String[] {"deck"};
	
	public void setValidDrawSources(String... in) {
		validDrawSources = in;
	}
	
	public DrawSource() {
		cardsToDraw = 2;
	}
	
	public DrawSource(int cardsToDraw) {
		this.cardsToDraw = cardsToDraw;
	}
	
	@Override
	public String getName() {
		return "drawSource";
	}
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		player.popAction();
		return new DrawSourceResponse(cardsToDraw, validDrawSources);
	}
}
