package com.jtriemstra.bang.api.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class DrawKitCarlsonRequest extends BaseRequest {

	private List<String> chosenCardNames;
	private List<UUID> chosenCardIds;
	
	@Override
	public String getActionName() {
		return "draw";
	}

}
