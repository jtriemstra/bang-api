package com.jtriemstra.bang.api.model.player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jtriemstra.bang.api.model.CardSet;
import com.jtriemstra.bang.api.model.card.Card;

public class Hand extends CardSet {
	
	public Card getByName(String name) {
		for(Card c : cards) {
			if (name.equals(c.getName())) {
				return c;
			}
		}
		
		throw new RuntimeException("card " + name + " not found in hand");
	}
	
	public int countByNames(String... names) {
		int result = 0;
		for (Card c : cards) {
			for (String s : names) {
				if (s.equals(c.getName())) {
					result++;
					break;
				}
			}
		}
		
		return result;
	}

	public boolean cardInHand(String name) {
		for(Card c : cards) {
			if (name.equals(c.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public Card remove(int index) {
		return cards.remove(index);
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public Card getById(UUID id) {
		Optional<Card> card = cards.stream().filter(c -> c.getId().equals(id)).findAny();
		if (card.isPresent()) {
			return card.get();
		}
		
		throw new RuntimeException("card " + id.toString() + " not found in hand");
	}

	public void clear() {
		cards.clear();
	}
}
