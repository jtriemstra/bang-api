package com.jtriemstra.bang.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.card.Card;

import lombok.Data;

@Data
@Component
public class IntegrationTestState {
	private List<String> lastCards;
}
