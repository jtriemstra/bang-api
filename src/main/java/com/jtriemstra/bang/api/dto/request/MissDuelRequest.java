package com.jtriemstra.bang.api.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class MissDuelRequest extends BaseRequest {

	private String cardName;
	private UUID cardId;
	
	@Override
	public String getActionName() {
		return "miss";
	}
	

}
