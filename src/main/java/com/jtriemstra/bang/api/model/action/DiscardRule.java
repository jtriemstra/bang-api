package com.jtriemstra.bang.api.model.action;

import com.jtriemstra.bang.api.dto.request.ActionRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DiscardRuleResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DiscardRule extends BaseAction {

	private BaseAction discardAction;
	private int discardCount = -1;
	private String name = "discardRule";
	private boolean popAction = true;
	
	public DiscardRule() {
		
	}
	
	public DiscardRule(BaseAction discardAction, int discardCount, String name, boolean popAction) {
		this.discardAction = discardAction;
		this.discardCount = discardCount;
		this.name = name;
		this.popAction = popAction;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public BaseResponse execute(ActionRequest request, Player player, Game game) {
		if (!popAction) {
			player.removeActionOption(name);
		}
		else {
			player.popAction();
		}
		
		if (discardCount == -1) {
			discardCount = Math.max(player.getHandSize() - player.getHealth(), 0);
			player.addNextAction(new Discard(discardCount));
		} else {
			player.addNextAction(discardAction);
		}
		return new DiscardRuleResponse(discardCount);
	}
}
