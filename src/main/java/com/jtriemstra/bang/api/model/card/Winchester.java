package com.jtriemstra.bang.api.model.card;

public class Winchester extends Gun {
	public Winchester(String denomination, String suit) {
		super(denomination, suit, "Winchester");
	}
	
	@Override
	public int getRange() {
		return 5;
	}

}
