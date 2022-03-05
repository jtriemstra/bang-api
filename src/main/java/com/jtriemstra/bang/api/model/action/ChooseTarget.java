package com.jtriemstra.bang.api.model.action;

import java.util.List;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.request.ChooseTargetRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.WaitingResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.TargetingCard;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class ChooseTarget extends BaseAction {
	private TargetingCard sourceCard;
	private List<Player> targets;
	
	public ChooseTarget(TargetingCard sourceCard, List<Player> targets) {
		this.sourceCard = sourceCard;
		this.targets = targets;
	}

	@Override
	public String getName() {
		return "chooseTarget";
	}
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		ChooseTargetRequest chooseTargetRequest = (ChooseTargetRequest) request;
		Player target = game.getPlayerById(chooseTargetRequest.getTargetId());
		
		game.notify("Player " + player.getName() + " targets " + target.getName());
		
		player.popAction();
		return sourceCard.doAttack(player, target, game);		
	}

}
