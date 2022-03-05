package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Quit extends BaseAction {

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		return null;
	}

}
