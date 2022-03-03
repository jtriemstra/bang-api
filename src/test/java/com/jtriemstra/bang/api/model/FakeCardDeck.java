package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Barrel;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Duel;
import com.jtriemstra.bang.api.model.card.GeneralStore;
import com.jtriemstra.bang.api.model.card.Indians;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Winchester;
import com.jtriemstra.bang.api.model.deck.CardDeck;

public class FakeCardDeck implements CardDeck {

private List<Card> cards;
	
	public FakeCardDeck() {
		cards = new ArrayList<>();
		//p1
		cards.add(new Bang("8", "Diamonds"));
		cards.add(new Bang("J", "Diamonds"));
		cards.add(new Bang("4", "Clubs"));
		cards.add(new Bang("7", "Diamonds"));
		cards.add(new Bang("9", "Clubs"));
		//p2
		cards.add(new Bang("10", "Diamonds"));
		cards.add(new Bang("5", "Diamonds"));
		cards.add(new Bang("4", "Diamonds"));
		//p3
		cards.add(new Bang("9", "Diamonds"));
		cards.add(new Bang("3", "Clubs"));
		cards.add(new Bang("K", "Hearts"));
		cards.add(new Bang("A", "Diamonds"));
		//p4
		cards.add(new Bang("6", "Diamonds"));
		cards.add(new Bang("8", "Clubs"));
		cards.add(new Bang("5", "Clubs"));
		cards.add(new Bang("K", "Diamonds"));
		// test player 1
		cards.add(new Bang("Q", "Hearts"));
		cards.add(new Bang("6", "Diamonds"));
		// test player 2
		cards.add(new Barrel("7", "Diamonds"));
		cards.add(new Missed("7", "Diamonds"));
		// test player 3
		cards.add(new Bang("6", "Clubs"));
		cards.add(new Winchester("8", "Clubs"));
		// test player 4
		cards.add(new GeneralStore("A", "Diamonds"));
		cards.add(new Indians("6", "Diamonds"));
		// general store
		cards.add(new Bang("8", "Diamonds"));
		cards.add(new Bang("J", "Diamonds"));
		cards.add(new Bang("4", "Clubs"));
		cards.add(new Bang("7", "Diamonds"));
		// test player 1
		cards.add(new Duel("Q", "Hearts"));
		cards.add(new Bang("6", "Clubs"));
	}
		
	public Card draw() {
		return cards.remove(0);
	}

	public void addToTop(Card c) {
		cards.add(0, c);
	}

	@Override
	public int getSize() {
		return cards.size();
	}

	@Override
	public void addAll(List<Card> cards) {
		this.cards.addAll(cards);
	}
}
