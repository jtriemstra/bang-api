package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.ActionList;
import com.jtriemstra.bang.api.model.action.DefenseOptions;
import com.jtriemstra.bang.api.model.action.DiscardRule;
import com.jtriemstra.bang.api.model.action.DrawKitCarlson;
import com.jtriemstra.bang.api.model.action.DrawSource;
import com.jtriemstra.bang.api.model.action.DrawSourcePedroRamirez;
import com.jtriemstra.bang.api.model.action.Play;

public class KitCarlson extends Character {
	public KitCarlson() {
		super("Kit Carlson", 4);
	}
	
	@Override
	public ActionList createInitialActions(ActionList in) {
		in.push(new Play(), new DiscardRule());
		in.push(new DrawKitCarlson());
		return in;
	}
}
