package com.jtriemstra.bang.api.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InitResponse {
	private String id;
}
