package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.action.ActionList;
import com.jtriemstra.bang.api.action.DefenseOptions;
import com.jtriemstra.bang.api.action.DiscardRule;
import com.jtriemstra.bang.api.action.Draw;
import com.jtriemstra.bang.api.action.DrawSource;
import com.jtriemstra.bang.api.action.DrawSourcePedroRamirez;
import com.jtriemstra.bang.api.action.Play;

public class PedroRamirez extends Character {
	public PedroRamirez() {
		super("Pedro Ramirez", 4);
	}
	
	@Override
	public ActionList createInitialActions(ActionList in) {
		in.push(new Play(), new DiscardRule());
		in.push(new DrawSourcePedroRamirez());
		return in;
	}
}
