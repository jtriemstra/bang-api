package com.jtriemstra.bang.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DrawRequest extends ActionRequest {
	private String sourceName;
	private int numberToDraw;
	
	@Override
	public String getActionName() {
		return "draw";
	}
}
