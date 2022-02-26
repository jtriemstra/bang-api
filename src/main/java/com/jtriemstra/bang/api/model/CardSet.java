package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.model.card.Card;

public class CardSet {
	protected List<Card> cards = new ArrayList<>();
	
	public Card add(Card c) {
		cards.add(c);
		return c;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public void remove(Card c) {
		cards.remove(c);
	}
	
	public List<Card> getCards() {
		return cards;
	}
}
