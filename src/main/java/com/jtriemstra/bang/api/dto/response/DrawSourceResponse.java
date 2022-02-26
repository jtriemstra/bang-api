package com.jtriemstra.bang.api.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DrawSourceResponse extends BaseResponse {
	private List<String> sourceNames;
	private int numberToDraw;
	
	public DrawSourceResponse(int numberToDraw, String... sourceNames) {
		this.numberToDraw = numberToDraw;
		this.sourceNames = new ArrayList<String>();
		for (String s : sourceNames) {
			this.sourceNames.add(s);
		}
	}
}
