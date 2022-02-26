package com.jtriemstra.bang.api.action;

import java.util.ArrayList;
import java.util.List;

import com.jtriemstra.bang.api.dto.request.BaseRequest;
import com.jtriemstra.bang.api.dto.response.DefenseOptionsResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

public class DefenseOptions extends BaseAction {
	protected Player attackingPlayer;
	private List<DefenseRule> defenseRules;
	private Card attackCard;
	
	public DefenseOptions(Player attackingPlayer, List<DefenseRule> defenseRules, Card attackCard) {
		this.attackingPlayer = attackingPlayer;
		this.defenseRules = defenseRules;
	}
	
	@Override
	public String getName() {
		return "defenseOptions";
	}

	@Override
	public DefenseOptionsResponse execute(BaseRequest request, Player player, Game game) {
		player.popAction();
		
		defenseRules.add(new DefenseRule("pass", ((defender, attacker, attackCard) -> {return true;}), actions -> actions.add(new Pass(attackingPlayer))));
		player.alterDefenseRules(defenseRules, attackingPlayer);
		
		List<BaseAction> options = new ArrayList<>();
		for (DefenseRule rule : defenseRules) {
			if (rule.getCondition().apply(player, attackingPlayer, attackCard)) {
				rule.getBehavior().accept(options);
			}
		}
		
		player.addNextAction(options.toArray(new BaseAction[options.size()]));
		
		// the array is a "final in lambda" workaround
		int[] numberOfCardsToDefend = new int[] {1};
		options.stream().filter(o -> o instanceof Play).forEach(o -> numberOfCardsToDefend[0] = ((Play) o).getNumberOfCardsToPlay());
		return new DefenseOptionsResponse(numberOfCardsToDefend[0]);
				
	}

}
