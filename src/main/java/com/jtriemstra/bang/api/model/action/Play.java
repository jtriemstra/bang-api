package com.jtriemstra.bang.api.model.action;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.PlayRequest;
import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.card.Playable;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.Setter;

public class Play extends BaseAction {
	protected String[] validCardNames;
	@Setter
	private Set<UUID> validCardIds;
	
	public Play() {
		super();
	}
	
	public Play(String... validCardNames) {
		super();
		this.validCardNames = validCardNames;
	}
	
	@Override
	public String getName() {
		return "play";
	}

	public int getNumberOfCardsToPlay() {
		return 1;
	}
	
//	public BiConsumer<PlayRequest, String[]> cardValidator = (playRequest, validCardNames) -> {
//		if (validCardNames != null) {
//			for (String s1 : playRequest.getCardNames()) {
//				if (Arrays.stream(validCardNames).anyMatch(s -> s.equals(s1))) {
//					throw new RuntimeException("card " + playRequest.getCardNames() + " is not valid to play now");
//				}
//			}
//		}
//	};
	
	@Override
	public PlayResponse execute(BaseRequest request, Player player, Game game) {
		PlayRequest playRequest = (PlayRequest) request;
		
		game.notify("Player " + player.getName() + " plays a " + playRequest.getCardNames()[0]);
		
		if (!Arrays.stream(playRequest.getCardIds()).allMatch(id -> validCardIds.contains(id))) {
			throw new RuntimeException("card " + playRequest.getCardNames() + " is not valid to play now");
		}
		
		if (getNumberOfCardsToPlay() != playRequest.getCardNames().length) {
			throw new RuntimeException("you need to play " + getNumberOfCardsToPlay() + " at the same time");
		}
		
		//NOTE: this assumes two cards being played at the same time can be treated the same, which I think is true
		Playable p = player.getPlayableById(playRequest.getCardIds()[0], this);
		PlayResponse o = p.play(player, game);
		
		for (UUID s : playRequest.getCardIds()) {
			Card c = player.getCardById(s);
			player.discard(c);
			if (!c.getTable()) {
				game.discard(c);
			}
		}
		
		return (PlayResponse) o;
	}

}
