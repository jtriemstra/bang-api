package com.jtriemstra.bang.api.action;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.jtriemstra.bang.api.model.player.Player;

public class Miss extends Play {
	private Player attackingPlayer;
			
	public Miss(Player attackingPlayer) {
		super("Missed");
		this.attackingPlayer = attackingPlayer;
	}

	public Miss(Player attackingPlayer, String... validCardsToDefend) {
		super(validCardsToDefend);
		this.attackingPlayer = attackingPlayer;
	}
		
	public Player getAttackingPlayer() {
		return attackingPlayer;
	}
	
	public Set<String> getValidCardsToDefend() {
		if (this.validCardNames != null && this.validCardNames.length > 0) {
			return Arrays.stream(this.validCardNames).collect(Collectors.toSet());
		}
		
		return Set.of("Missed");
	}
}
