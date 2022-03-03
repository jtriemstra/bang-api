package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.ActionList;
import com.jtriemstra.bang.api.model.action.DiscardRule;
import com.jtriemstra.bang.api.model.action.Draw;
import com.jtriemstra.bang.api.model.action.DrawSourceJesseJones;
import com.jtriemstra.bang.api.model.action.Play;

public class JesseJones extends Character {
	public JesseJones() {
		super("Jesse Jones", 4);
	}
	
	@Override
	public ActionList createInitialActions(ActionList in) {
		in.push(new Play(), new DiscardRule());
		in.push(new Draw(1, "deck"));
		in.push(new DrawSourceJesseJones());		
		return in;
	}
}
