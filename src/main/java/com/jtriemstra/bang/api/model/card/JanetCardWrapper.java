package com.jtriemstra.bang.api.model.card;

import java.util.UUID;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class JanetCardWrapper implements Playable {
	
	private Card c;
	private boolean canPlayBang;
	private boolean canPlayMissed;
	
	public JanetCardWrapper(Card c, boolean canPlayBang, boolean canPlayMissed) {
		this.c = c;
		this.canPlayBang = canPlayBang;
		this.canPlayMissed = canPlayMissed;
	}

	@Override
	public String getName() {
		if (canPlayBang && c instanceof Missed) {
			return "Missed (as Bang)";
		}
		else if (canPlayMissed && c instanceof Bang) {
			return "Bang (as Missed)";
		}
		else {
			return c.getName();
		}
	}

	@Override
	public String getSuit() {
		return c.getSuit();
	}

	@Override
	public String getDenomination() {
		return c.getDenomination();
	}

	@Override
	public UUID getId() {
		return c.getId();
	}

	@Override
	public boolean getTable() {
		return c.getTable();
	}

	@Override
	public PlayResponse play(Player player, Game game) {
		if (canPlayBang && c instanceof Missed) {
			Card temp = new Bang(c.getDenomination(), c.getSuit());
			return temp.play(player, game);
		}
		else if (canPlayMissed && c instanceof Bang) {
			Card temp = new Missed(c.getDenomination(), c.getSuit());
			return temp.play(player, game);
		}
		else {
			return c.play(player, game);
		}		
	}
}
