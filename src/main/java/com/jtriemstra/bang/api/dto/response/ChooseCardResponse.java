package com.jtriemstra.bang.api.dto.response;

import java.util.List;

import com.jtriemstra.bang.api.model.card.Card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChooseCardResponse extends PlayResponse {
	private List<Card> cards;
	private int numberToChoose;
}
