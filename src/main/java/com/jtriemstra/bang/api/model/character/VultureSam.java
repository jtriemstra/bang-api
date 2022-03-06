package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class VultureSam extends Character {
	public VultureSam() {
		super("Vulture Sam", 4);
	}
	
	@Override
	public void init(Player p, Game g) {
		g.addPlayerEventListener("death", deadPlayer -> {
			deadPlayer.getHand().getCards().forEach(c -> {p.gain(c); deadPlayer.discard(c);});
		});
	}
}
