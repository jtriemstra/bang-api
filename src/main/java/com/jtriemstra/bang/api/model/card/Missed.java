package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.response.TargetingCardResponse;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.action.ChooseTarget;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.Miss;
import com.jtriemstra.bang.api.model.player.Player;


public class Missed extends Card {

	public Missed(String denomination, String suit) {
		super(denomination, suit, "Missed");
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		Miss action = (Miss) player.popAction().getByName("play");
		action.getAttackingPlayer().unwait();
		return new PlayResponse();
	}

}
