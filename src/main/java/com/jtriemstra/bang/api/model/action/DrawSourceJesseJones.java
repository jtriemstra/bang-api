package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DrawSourceJesseJones extends DrawSource {
	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		player.popAction();
		
		Player[] players = game.getPlayers().getArray();
		String[] names = new String[players.length];
		names[0] = "deck";
		int index = 1;
		for (Player p : players) {
			if (player != p && p.getHandSize() > 0) {
				names[index] = p.getName();
				index++;
			}
		}
		player.addNextAction(new Draw(1, names));
		
		return new DrawSourceResponse(1, names);
	}
}
