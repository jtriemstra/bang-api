package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class ElGringo extends Character {
	public ElGringo() {
		super("El Gringo", 3);
	}
	
	@Override
	public void doHit(Player attacker, Player defender) {
		Card c = attacker.loseCardFromHand();
		defender.gain(c);
		
		defender.addMessage("You gained a " + c.getName() + " card");
		attacker.addMessage("El Gringo took " + c.getName());
	}
}
