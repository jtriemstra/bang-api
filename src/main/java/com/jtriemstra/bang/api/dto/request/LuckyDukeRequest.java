package com.jtriemstra.bang.api.dto.request;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LuckyDukeRequest extends BaseRequest {
	
	private String action;
	private UUID useId;
	private UUID discardId;
	
	@Override
	public String getActionName() {
		return action;
	}
}
