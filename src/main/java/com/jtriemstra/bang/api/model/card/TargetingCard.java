package com.jtriemstra.bang.api.model.card;

import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public interface TargetingCard {
	public BaseResponse doAttack(Player attacker, Player defender, Game game);
}
