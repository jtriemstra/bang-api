package com.jtriemstra.bang.api.dto.request;

public class DiscardRuleRequest extends ActionRequest {

	private String actionName = "discardRule";
	
	public ActionRequest setActionName(String in) {
		actionName = in;
		return this;
	}
	
	@Override
	public String getActionName() {
		return actionName;
	}
}
