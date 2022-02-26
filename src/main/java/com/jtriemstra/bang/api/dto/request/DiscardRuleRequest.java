package com.jtriemstra.bang.api.dto.request;

public class DiscardRuleRequest extends BaseRequest {

	private String actionName = "discardRule";
	
	public BaseRequest setActionName(String in) {
		actionName = in;
		return this;
	}
	
	@Override
	public String getActionName() {
		return actionName;
	}
}
