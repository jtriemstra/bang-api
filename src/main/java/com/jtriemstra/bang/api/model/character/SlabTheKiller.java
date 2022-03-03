package com.jtriemstra.bang.api.model.character;

import com.jtriemstra.bang.api.model.action.Miss;
import com.jtriemstra.bang.api.model.action.MissSlab;
import com.jtriemstra.bang.api.model.card.Bang;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class SlabTheKiller extends Character {
	public SlabTheKiller() {
		super("Slab The Killer", 4);
	}
		
	@Override
	public Miss getMissAction(Player thisPlayer, Card attackCard) {
		if (attackCard instanceof Bang) {
			return new MissSlab(thisPlayer);
		}
		
		return new Miss(thisPlayer);
	}
}
