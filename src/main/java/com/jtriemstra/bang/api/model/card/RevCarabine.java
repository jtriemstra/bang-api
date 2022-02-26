package com.jtriemstra.bang.api.model.card;

public class RevCarabine extends Gun {
	public RevCarabine(String denomination, String suit) {
		super(denomination, suit, "Rev Carabine");
	}
	
	@Override
	public int getRange() {
		return 4;
	}

}
