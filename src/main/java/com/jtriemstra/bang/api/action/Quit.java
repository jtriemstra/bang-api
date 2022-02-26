package com.jtriemstra.bang.api.action;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Quit extends BaseAction {

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		return null;
	}

}
