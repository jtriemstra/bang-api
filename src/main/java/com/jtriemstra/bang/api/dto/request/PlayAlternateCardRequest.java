package com.jtriemstra.bang.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayAlternateCardRequest extends PlayRequest {
	private String[] alternateCardNames;
}
