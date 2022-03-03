package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.ActionList;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.DiscardRule;
import com.jtriemstra.bang.api.model.action.Draw;
import com.jtriemstra.bang.api.model.action.DrawSource;
import com.jtriemstra.bang.api.model.action.DrawSourcePedroRamirez;
import com.jtriemstra.bang.api.model.action.Play;

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
