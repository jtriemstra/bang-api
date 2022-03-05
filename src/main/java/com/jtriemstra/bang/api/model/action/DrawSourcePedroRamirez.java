package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DrawSourcePedroRamirez extends DrawSource {
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		player.popAction();
		
		String[] validDrawSources;
		
		if (game.getDiscard().getSize() > 0) {
			validDrawSources = new String[] {"deck", "discard"};
			player.addNextAction(new Draw(1, "deck"));
			player.addNextAction(new Draw(1, validDrawSources));
			return new DrawSourceResponse(1, validDrawSources);
		}
		else {
			validDrawSources = new String[] {"deck"};
			player.addNextAction(new Draw(2, "deck"));
			return new DrawSourceResponse(2, validDrawSources);
		}
	}
}
