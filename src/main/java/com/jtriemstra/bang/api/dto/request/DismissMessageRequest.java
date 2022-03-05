package com.jtriemstra.bang.api.dto.request;

public class DismissMessageRequest extends ActionRequest {
	@Override
	public String getActionName() {
		return "dismissMessage";
	}
}
