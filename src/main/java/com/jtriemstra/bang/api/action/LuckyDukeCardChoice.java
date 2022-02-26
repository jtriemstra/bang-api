package com.jtriemstra.bang.api.action;

import java.util.List;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.request.LuckyDukeRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class LuckyDukeCardChoice extends BaseAction {
	
	private BaseAction innerAction;
	private List<Card> options;
	
	public LuckyDukeCardChoice(BaseAction innerAction, List<Card> options) {
		this.innerAction = innerAction;
		this.options = options;
	}
	
	@Override
	public String getName() {
		return innerAction.getName();
	}

	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		LuckyDukeRequest luckyRequest = (LuckyDukeRequest) request;
		
		((CardCheckFlag) innerAction).setCardBehavior(g -> {
			Card c = options.stream().filter(o -> o.getId().equals(luckyRequest.getUseId())).findFirst().get();
			g.discard(c);
			g.discard(options.stream().filter(o -> o.getId().equals(luckyRequest.getDiscardId())).findFirst().get());
			return c;
		});
	
		player.popAction();
		
		return innerAction.execute(request, player, game);
	}

}
