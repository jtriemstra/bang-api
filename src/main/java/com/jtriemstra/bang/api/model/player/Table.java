package com.jtriemstra.bang.api.model.player;

import com.jtriemstra.bang.api.model.CardSet;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Gun;

public class Table extends CardSet {
	public Gun getGun() {
		for (Card c : cards) {
			if (c instanceof Gun) {
				return (Gun) c;
			}
		}
		return null;
	}
	
	public boolean hasCard(String name) {
		for (Card c : cards) {
			if (name.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		cards.clear();
	}
}
