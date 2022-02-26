package com.jtriemstra.bang.api.dto.request;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChooseCardRequest extends BaseRequest {
	
	private String cardName;
	private String[] cardNames;
	private UUID cardId;
	private UUID[] cardIds;

	@Override
	public String getActionName() {
		return "chooseCard";
	}

	
}
