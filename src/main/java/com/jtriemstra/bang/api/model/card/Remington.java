package com.jtriemstra.bang.api.model.card;

public class Remington extends Gun {
	public Remington(String denomination, String suit) {
		super(denomination, suit, "Remington");
	}
	
	@Override
	public int getRange() {
		return 3;
	}

}
