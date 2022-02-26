package com.jtriemstra.bang.api.dto.response;

import com.jtriemstra.bang.api.model.card.Card;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StealResponse extends BaseResponse {
	private Card card;
}
