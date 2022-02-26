package com.jtriemstra.bang.api.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DiscardRuleResponse extends BaseResponse {
	private int numberToDiscard;
	
	public DiscardRuleResponse(int numberToDiscard) {
		this.numberToDiscard = numberToDiscard;
	}
}
