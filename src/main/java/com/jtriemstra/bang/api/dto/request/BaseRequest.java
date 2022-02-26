package com.jtriemstra.bang.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseRequest {
	private String playerId;
	private String gameName;
	private String actionName;
}
