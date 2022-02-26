package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.action.DefenseOptions;

public class PaulRegret extends Character {
	public PaulRegret() {
		super("Paul Regret", 3);
	}
	
	@Override
	public int getRangeWhenOnDefense() {
		return 1;
	}
}
