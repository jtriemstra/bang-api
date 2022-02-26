package com.jtriemstra.bang.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChooseTargetRequest extends BaseRequest {
	private String targetId;
	
	@Override
	public String getActionName() {
		return "chooseTarget";
	}
}
