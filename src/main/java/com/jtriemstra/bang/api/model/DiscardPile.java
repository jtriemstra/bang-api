package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.model.card.Card;

public class DiscardPile extends CardSet {

	public Card removeTop() {
		return cards.remove(cards.size() - 1);
	}

	public List<Card> clearAll() {
		List<Card> results = new ArrayList<>();
		results.addAll(cards);
		cards.clear();
		return results;
	}

}
