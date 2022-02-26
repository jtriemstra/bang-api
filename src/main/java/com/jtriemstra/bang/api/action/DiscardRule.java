package com.jtriemstra.bang.api.action;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.response.BaseResponse;
import com.jtriemstra.bang.api.dto.response.DiscardRuleResponse;
import com.jtriemstra.bang.api.dto.response.DrawSourceResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public class DiscardRule extends BaseAction {

	private BaseAction discardAction;
	private int discardCount = -1;
	private String name = "discardRule";
	
	public DiscardRule() {
		
	}
	
	public DiscardRule(BaseAction discardAction, int discardCount) {
		this.discardAction = discardAction;
		this.discardCount = discardCount;
		this.name = "discardRuleSid";
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public BaseResponse execute(BaseRequest request, Player player, Game game) {
		//TODO: clean out the sid references
		if (name.equals("discardRuleSid")) {
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
