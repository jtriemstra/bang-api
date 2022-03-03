package com.jtriemstra.bang.api.dto.response;

import java.util.Map;
import java.util.UUID;

import com.jtriemstra.bang.api.model.action.PossibleActions;
import com.jtriemstra.bang.api.model.card.Playable;
import com.jtriemstra.bang.api.model.state.GameState;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseResponse {
	private PossibleActions nextActions;
	protected Map<UUID, Playable> validCards;
	private GameState state;

	public String getNextActions() {
		return nextActions.toString();
	}
}
