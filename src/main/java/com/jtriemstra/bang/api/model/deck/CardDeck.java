package com.jtriemstra.bang.api.model.deck;

import java.util.List;

import com.jtriemstra.bang.api.model.card.Card;

public interface CardDeck {

	Card draw();

	void addToTop(Card c);

	int getSize();

	void addAll(List<Card> cards);
}
