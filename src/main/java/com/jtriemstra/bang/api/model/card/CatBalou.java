package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.action.ChooseTarget;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class CatBalou extends Card implements TargetingCard {
	
	public CatBalou(String denomination, String suit) {
		super(denomination, suit, "Cat Balou");
	}

	@Override
	public PlayResponse play(Player p, Game game) {
		List<Player> result = new ArrayList<Player>();
		for (Player p1 : game.getPlayers().getArray()) {
			if (p1 != p) {
				result.add(p1);
			}
		}
		
		List<String> targetNames = new ArrayList<String>();
		for (Player p1 : result) {
			targetNames.add(p1.getName());
		}
		
		p.addNextAction(new ChooseTarget(this, result));
		
		return new TargetingCardResponse(targetNames);
	}

	@Override
	public BaseResponse doAttack(Player attacker, Player defender, Game game) {
		Card c = defender.loseCardFromHand();
		game.discard(c);
		
		defender.addMessage("You have lost the " + c.getName());
		
		return new BaseResponse();
	}

}
