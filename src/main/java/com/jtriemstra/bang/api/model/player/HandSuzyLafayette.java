package com.jtriemstra.bang.api.model.player;

import com.jtriemstra.bang.api.model.CardSet;
import com.jtriemstra.bang.api.model.card.Card;

public class HandSuzyLafayette extends Hand {
	private Player thisPlayer;
	
	public HandSuzyLafayette(Player thisPlayer) {
		this.thisPlayer = thisPlayer;
	}
		
	@Override
	public void remove(Card c) {
		super.remove(c);
		if (cards.size() == 0) {
			thisPlayer.draw();
		}
	}
	
	@Override
	public Card remove(int index) {
		Card c = super.remove(index);
		if (cards.size() == 0) {
			thisPlayer.draw();
		}
		return c;
	}
}
