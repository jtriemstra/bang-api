package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.model.player.Player;

public class PlayerList {
	private List<Player> players = new ArrayList<>();
	
	public Player getById(String playerId) {
		for (Player p : players) {
			if (p.getName().equals(playerId)) {
				return p;
			}
		}
		
		throw new RuntimeException("player with id " + playerId + " not found");
	}

	public int size() {
		return players.size(); 
	}
	
	public int getIndex(Player p) {
		for (int i=0; i<players.size(); i++) {
			if (p == players.get(i)) {
				return i;
			}
		}
		
		throw new RuntimeException("player object was not found in list");
	}
	
	public Player getByIndex(int i) {
		return players.get(i);
	}

	void add(Player player) {
		players.add(player);
	}

	public Player[] getArray() {
		Player[] result = new Player[players.size()];
		return players.toArray(result);		
	}

	public Player getNextPlayer(Player p) {
		for (int i=0; i<players.size(); i++) {
			if (players.get(i) == p) {
				if (i < players.size() - 1) {
					return players.get(i + 1);
				}
				else {
					return players.get(0);
				}
			}
		}
		
		throw new RuntimeException("current player not found");
	}

	public void remove(Player player) {
		players.removeIf(p -> p.getName().equals(player.getName()));
	}
}
