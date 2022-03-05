package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DismissMessage extends BaseAction {

	@Override
	public String getName() {
		return "dismissMessage";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		player.removeActionOption(getName());
		player.getMessages().clear();
		
		return new BaseResponse();
	}

}
