package com.jtriemstra.bang.api.dto.request;

public class DismissMessageRequest extends BaseRequest {
	@Override
	public String getActionName() {
		return "dismissMessage";
	}
}
