package com.jtriemstra.bang.api.model.action;

import java.util.function.Function;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BarrelResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.Setter;

public class Barrel extends BaseAction implements CardCheckFlag {
	private Player attackingPlayer;
	
	public Barrel(Player attackingPlayer) {
		this.attackingPlayer = attackingPlayer;
	}
	
	@Override
	public String getName() {
		return "barrel";
	}

	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
				
		Card c = cardBehavior.apply(game);
		
		if (c.getSuit().equals("Hearts")) {
			game.notify("Player " + player.getName() + " uses a barrel successfully");
			attackingPlayer.unwait();
			player.popAction();
		}
		else {
			game.notify("Player " + player.getName() + " uses a barrel unsuccessfully");
			player.removeActionOption("barrel");
		}
		
		return new BarrelResponse();
	}

	@Setter
	Function<Game, Card> cardBehavior = game -> {
		Card c = game.draw();
		game.discard(c);
		return c;
	};
}
