package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.action.DefenseOptions;

public class RoseDoolan extends Character {
	public RoseDoolan() {
		super("Rose Doolan", 4);
	}
	
	@Override
	public int getRangeWhenOnOffense() {
		return 1;
	}
}
