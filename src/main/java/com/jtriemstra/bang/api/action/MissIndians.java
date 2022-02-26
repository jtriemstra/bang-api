package com.jtriemstra.bang.api.action;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class MissIndians extends Miss {
	public MissIndians(Player attackingPlayer) {
		super(attackingPlayer, "Bang");
	}
	
	@Override
	public PlayResponse execute(BaseRequest request, Player player, Game game) {
		PlayRequest missRequest = (PlayRequest) request;
		Card c = player.getCardById(missRequest.getCardIds()[0]);
		
		game.notify("Player " + player.getName() + " discards a " + missRequest.getCardNames()[0]);
		player.discard(c);
		game.discard(c);
		
		MissIndians action = (MissIndians) player.popAction().getByName(missRequest.getActionName());
		player.addNextAction(new Waiting());
		action.getAttackingPlayer().popAction();
		
		return new PlayResponse();
	}
}
