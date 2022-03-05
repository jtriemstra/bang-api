package com.jtriemstra.bang.api.dto.request;

public class WaitingRequest extends ActionRequest {
	@Override
	public String getActionName() {
		return "wait";
	}
}
