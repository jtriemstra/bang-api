package com.jtriemstra.bang.api.model.card;

public class Schofield extends Gun {
	public Schofield(String denomination, String suit) {
		super(denomination, suit, "Schofield");
	}
	
	@Override
	public int getRange() {
		return 2;
	}
}
