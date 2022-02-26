package com.jtriemstra.bang.api.model.card;

import java.util.UUID;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public interface Playable {

	String getName();

	String getSuit();

	String getDenomination();

	UUID getId();

	boolean getTable();
	
	PlayResponse play(Player player, Game game);
	
}
