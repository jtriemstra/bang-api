package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Pass extends BaseAction {
	private Player attackingPlayer;
	
	public Pass(Player attackingPlayer) {
		this.attackingPlayer = attackingPlayer;
	}
	
	@Override
	public String getName() {
		return "pass";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		game.notify("Player " + player.getName() + " does not defend");
				
		player.popAction();
		if (!player.loseHealthAndCheckEndGame(attackingPlayer)) {
			attackingPlayer.unwait();
		}
		
		return new BaseResponse();
	}

}
