package com.jtriemstra.bang.api.action;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Duel;
import com.jtriemstra.bang.api.model.player.Player;

public class MissDuel extends Miss {
	public MissDuel(Player attackingPlayer) {
		super(attackingPlayer, "Bang");
	}
	
	@Override
	public PlayResponse execute(BaseRequest request, Player player, Game game) {
		PlayRequest missRequest = (PlayRequest) request;
		Card c = player.getCardById(missRequest.getCardIds()[0]);
		player.discard(c);
		game.discard(c);
		
		game.notify("Player " + player.getName() + " plays a " + missRequest.getCardNames()[0]);
		
		MissDuel action = (MissDuel) player.popAction().getByName(missRequest.getActionName());
		player.addNextAction(new Waiting());
		action.getAttackingPlayer().popAction();
		
		List<DefenseRule> defenseRules = new ArrayList<>();
		defenseRules.add(new DefenseRule(DefenseRule.TYPE_MISSED, (defender, attacker, attackCard) -> defender.countCardsInHand("Bang") > 0, actions -> actions.add(new MissDuel(player))));
		action.getAttackingPlayer().addNextAction(new DefenseOptions(player, defenseRules, new Duel("dummy", "dummy")));
		
		return new PlayResponse();
	}
}
