package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.Data;

@Data
public abstract class BaseAction {
	public abstract String getName();
	public abstract BaseResponse execute(ActionRequest request, Player player, Game game);
}
