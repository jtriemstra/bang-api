package com.jtriemstra.bang.api.model.card;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.action.ChooseCardGeneralStore;
import com.jtriemstra.bang.api.action.Waiting;
import com.jtriemstra.bang.api.action.WaitingGeneralStore;
import com.jtriemstra.bang.api.dto.response.ChooseCardResponse;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class GeneralStore extends Card {

	public GeneralStore(String denomination, String suit) {
		super(denomination, suit, "General Store");
	}
	
	@Override
	public PlayResponse play(Player player, Game game) {
		List<Card> cards = new ArrayList<>();
		for (int i=0; i<game.getPlayers().size(); i++) {
			cards.add(game.draw());
		}
		
		//this waiting will get popped by the last call to DrawGeneralStore
		player.addNextAction(new Waiting());
		ChooseCardGeneralStore action = new ChooseCardGeneralStore(cards); 
		player.addNextAction(action);
		for (Player p : game.getPlayers().getArray()) {
			if (p != player) {
				p.addNextAction(new WaitingGeneralStore());
			}
		}
		
		return new ChooseCardResponse(cards, 1);
	}

}
