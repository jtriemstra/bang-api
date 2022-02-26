package com.jtriemstra.bang.api.dto.response;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class DefenseOptionsResponse extends BaseResponse {
	private int numberOfCardsToDefend;
	
	public DefenseOptionsResponse(int numberOfCards) {
		this.numberOfCardsToDefend = numberOfCards;
	}
	
}
