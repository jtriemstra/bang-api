package com.jtriemstra.bang.api.model.card;

import java.util.UUID;

import com.jtriemstra.bang.api.dto.response.PlayResponse;
import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.player.Player;

public abstract class Card implements Playable {
	private String suit;
	private String denomination;
	private String name;
	private UUID id;
	
	public Card(String denomination, String suit, String name) {
		this.suit = suit;
		this.denomination = denomination;
		this.name = name;
		this.id = UUID.nameUUIDFromBytes((name + " " + denomination + " " + suit).getBytes());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getSuit() {
		return suit;
	}
	
	@Override
	public String getDenomination() {
		return denomination;
	}
	
	@Override
	public UUID getId() {
		return id;
	}
	

	@Override
	public boolean getTable() {
		return false;
	}
}

