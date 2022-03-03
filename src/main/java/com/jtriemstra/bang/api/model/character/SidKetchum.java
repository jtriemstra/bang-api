package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.action.ActionList;
import com.jtriemstra.bang.api.action.DiscardRule;
import com.jtriemstra.bang.api.action.DiscardSidKetchum;
import com.jtriemstra.bang.api.model.player.Player;

public class SidKetchum extends Character {
	public SidKetchum() {
		super("Sid Ketchum", 4);
	}

	@Override
	public void addActions(Player p, ActionList actions) {
		//TODO: there may be an odd edge case where you want to play this in the middle of a normal discard process...
		if (p.getHandSize() >= 2 && p.getCurrentHealth() < p.getMaxHealth() && !actions.getNext().contains("discardSidKetchum") && !actions.getNext().contains("discard")) {
			actions.getNext().addAction(new DiscardRule(new DiscardSidKetchum(), 2, "discardRuleSid", false));
		} else if (actions.getNext().contains("discardRuleSid")) {
			actions.getNext().removeAction("discardRuleSid");
		}
	}
}
