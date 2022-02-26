package com.jtriemstra.bang.api.dto.response;

import java.util.List;

import com.jtriemstra.bang.api.model.card.Card;

import lombok.Data;

@Data
public class DrawResponse extends BaseResponse {
	List<Card> cards;
	
	public DrawResponse(List<Card> cards) {
		this.cards = cards;
	}
}
