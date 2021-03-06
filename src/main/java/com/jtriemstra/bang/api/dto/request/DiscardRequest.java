package com.jtriemstra.bang.api.dto.request;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DiscardRequest extends ActionRequest {
	private String[] cardNames;
	private UUID[] cardIds;
	
	@Override
	public String getActionName() {
		return "discard";
	}
}
