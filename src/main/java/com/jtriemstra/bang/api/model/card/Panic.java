package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.action.ChooseTarget;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.dto.response.StealResponse;
import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class Panic extends Card implements TargetingCard {
	
	public Panic(String denomination, String suit) {
		super(denomination, suit, "Panic");
	}

	@Override
	public PlayResponse play(Player p, Game game) {
		List<Player> targets = p.getTargets(1);
		List<String> targetNames = new ArrayList<String>();
		for (Player p1 : targets) {
			targetNames.add(p1.getName());
		}
		
		p.addNextAction(new ChooseTarget(this, targets));
		return new TargetingCardResponse(targetNames);
	}

	@Override
	public BaseResponse doAttack(Player attacker, Player defender, Game game) {
		Card c = defender.loseCardFromHand();
		attacker.gain(c);
		
		game.notify(attacker.getName() + " steals a card from " + defender.getName());
		defender.addMessage(c.getName() + " card was stolen");
		
		return new StealResponse().setCard(c);
	}

}
