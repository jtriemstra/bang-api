package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.ActionList;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.DiscardRule;
import com.jtriemstra.bang.api.model.action.Draw;
import com.jtriemstra.bang.api.model.action.DrawBlackJack;
import com.jtriemstra.bang.api.model.action.DrawSource;
import com.jtriemstra.bang.api.model.action.DrawSourcePedroRamirez;
import com.jtriemstra.bang.api.model.action.Play;

public class BlackJack extends Character {
	public BlackJack() {
		super("Black Jack", 4);
	}
	
	@Override
	public ActionList createInitialActions(ActionList in) {
		in.push(new Play(), new DiscardRule());
		in.push(new DrawBlackJack());
		return in;
	}
}
