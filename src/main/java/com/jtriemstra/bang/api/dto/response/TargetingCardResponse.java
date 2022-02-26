package com.jtriemstra.bang.api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TargetingCardResponse extends PlayResponse {
	private List<String> targets;
}
