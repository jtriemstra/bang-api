package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.ChooseCardResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class WaitingGeneralStore extends BaseAction {
	
	private ChooseCardGeneralStore action;
	
	public WaitingGeneralStore() {
		
	}
	
	public WaitingGeneralStore(ChooseCardGeneralStore action) {
		this.action = action;
	}
	
	@Override
	public String getName() {
		return "wait";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		if (action != null) {
			player.popAction();
			player.addNextAction(action);
			return new ChooseCardResponse(action.cards, 1);
		}
		else {
			return new WaitingResponse();
		}
	}

}
