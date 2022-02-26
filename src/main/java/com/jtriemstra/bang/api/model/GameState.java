package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.model.player.Player;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameState {
	private CurrentPlayerState currentPlayer;
	private List<OtherPlayerState> otherPlayers;
	
	public GameState(Game game, Player currentPlayer) {
		this.currentPlayer = new CurrentPlayerState(currentPlayer);
		this.otherPlayers = new ArrayList<>();
		
		Player[] players = game.getPlayers().getArray();
		int playerIndex = 0;
		while(playerIndex < players.length && currentPlayer.getName() != players[playerIndex].getName()) {
			playerIndex++;
		}
		playerIndex = (playerIndex == players.length - 1 ? 0 : playerIndex+1);
		while(playerIndex < players.length && currentPlayer.getName() != players[playerIndex].getName()) {
			otherPlayers.add(new OtherPlayerState(players[playerIndex]));
			playerIndex = (playerIndex == players.length - 1 ? 0 : playerIndex+1);			
		}
	}
}
