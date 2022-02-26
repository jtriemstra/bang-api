package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Barrel;
import com.jtriemstra.bang.api.model.card.Beer;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.CatBalou;
import com.jtriemstra.bang.api.model.card.Duel;
import com.jtriemstra.bang.api.model.card.Gatling;
import com.jtriemstra.bang.api.model.card.GeneralStore;
import com.jtriemstra.bang.api.model.card.Indians;
import com.jtriemstra.bang.api.model.card.Missed;
import com.jtriemstra.bang.api.model.card.Mustang;
import com.jtriemstra.bang.api.model.card.Panic;
import com.jtriemstra.bang.api.model.card.Remington;
import com.jtriemstra.bang.api.model.card.RevCarabine;
import com.jtriemstra.bang.api.model.card.Saloon;
import com.jtriemstra.bang.api.model.card.Schofield;
import com.jtriemstra.bang.api.model.card.Scope;
import com.jtriemstra.bang.api.model.card.Stagecoach;
import com.jtriemstra.bang.api.model.card.Volcanic;
import com.jtriemstra.bang.api.model.card.WellsFargo;
import com.jtriemstra.bang.api.model.card.Winchester;

public class RealCardDeck implements CardDeck {

private List<Card> cards;
	
	public RealCardDeck() {
		cards = new ArrayList<>();
		cards.add(new Duel("J", "Spades"));
		cards.add(new Duel("8", "Clubs"));
		cards.add(new Duel("Q", "Diamonds"));
		cards.add(new Indians("A","Diamonds"));
		cards.add(new Indians("K","Diamonds"));
		cards.add(new GeneralStore("9","Clubs"));
		cards.add(new GeneralStore("Q","Spades"));
		cards.add(new Barrel("Q","Spades"));
		cards.add(new Barrel("K","Spades"));
		cards.add(new Scope("A","Spades"));
		cards.add(new Mustang("9","Hearts"));
		cards.add(new Mustang("8","Hearts"));
		cards.add(new Beer("6","Hearts"));
		cards.add(new Beer("7","Hearts"));
		cards.add(new Beer("8","Hearts"));
		cards.add(new Beer("8","Hearts"));
		cards.add(new Beer("10","Hearts"));
		cards.add(new Beer("J","Hearts"));
		cards.add(new Panic("Q","Hearts"));
		cards.add(new Panic("A","Hearts"));
		cards.add(new Panic("J","Hearts"));
		cards.add(new Panic("8","Diamonds"));
		cards.add(new Gatling("10","Hearts"));
		cards.add(new Stagecoach("9","Spades"));
		cards.add(new Stagecoach("9","Spades"));
		cards.add(new WellsFargo("3","Hearts"));
		cards.add(new Saloon("5","Hearts"));
		cards.add(new CatBalou("K","Hearts"));
		cards.add(new CatBalou("9","Diamonds"));
		cards.add(new CatBalou("10","Diamonds"));
		cards.add(new CatBalou("J","Diamonds"));
		cards.add(new Missed("10","Clubs"));
		cards.add(new Missed("J","Clubs"));
		cards.add(new Missed("Q","Clubs"));
		cards.add(new Missed("K","Clubs"));
		cards.add(new Missed("A","Clubs"));
		cards.add(new Missed("2","Spades"));
		cards.add(new Missed("3","Spades"));
		cards.add(new Missed("4","Spades"));
		cards.add(new Missed("5","Spades"));
		cards.add(new Missed("6","Spades"));
		cards.add(new Missed("7","Spades"));
		cards.add(new Missed("8","Spades"));
		cards.add(new Volcanic("10","Clubs"));
		cards.add(new Schofield("J","Clubs"));
		cards.add(new Schofield("Q","Clubs"));
		cards.add(new Remington("K","Clubs"));
		cards.add(new RevCarabine("A","Clubs"));
		cards.add(new Volcanic("10","Spades"));
		cards.add(new Schofield("K","Spades"));
		cards.add(new Winchester("8","Spades"));
		cards.add(new Bang("A","Diamonds"));
		cards.add(new Bang("K","Diamonds"));
		cards.add(new Bang("Q","Diamonds"));
		cards.add(new Bang("J","Diamonds"));
		cards.add(new Bang("10","Diamonds"));
		cards.add(new Bang("9","Diamonds"));
		cards.add(new Bang("8","Diamonds"));
		cards.add(new Bang("7","Diamonds"));
		cards.add(new Bang("6","Diamonds"));
		cards.add(new Bang("5","Diamonds"));
		cards.add(new Bang("4","Diamonds"));
		cards.add(new Bang("3","Diamonds"));
		cards.add(new Bang("2","Diamonds"));
		cards.add(new Bang("A","Hearts"));
		cards.add(new Bang("K","Hearts"));
		cards.add(new Bang("Q","Hearts"));
		cards.add(new Bang("A","Spades"));
		cards.add(new Bang("9","Clubs"));
		cards.add(new Bang("8","Clubs"));
		cards.add(new Bang("7","Clubs"));
		cards.add(new Bang("6","Clubs"));
		cards.add(new Bang("5","Clubs"));
		cards.add(new Bang("4","Clubs"));
		cards.add(new Bang("3","Clubs"));
		cards.add(new Bang("2","Clubs"));
		// TODO: add these cards
//		cards.add(new Dynamite("2","Hearts"));
//		cards.add(new Jail("J", "Spades"));
//		cards.add(new Jail("10", "Spades"));
//		cards.add(new Jail("4", "Hearts"));
	}
		
	public Card draw() {
		int index = (new Random()).nextInt(cards.size());
		return cards.remove(index);
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
