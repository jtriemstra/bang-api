package com.jtriemstra.bang.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JoinRequest {
	private String playerName;
	private String gameName;
}
