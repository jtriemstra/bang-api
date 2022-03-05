package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Waiting extends BaseAction {
	
	@Override
	public String getName() {
		return "wait";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		
		return new WaitingResponse();
	}

}
