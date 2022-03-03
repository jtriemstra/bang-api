package com.jtriemstra.bang.api.model.state;

import java.util.List;

import com.jtriemstra.bang.api.model.player.Hand;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.player.Table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrentPlayerState {
	private String name;
	private int currentHealth, maxHealth;
	private String gun;
	private String character;
	private Table table;
	private Hand hand;
	private String role;
	private List<String> messages;
	
	public CurrentPlayerState(Player p) {
		name = p.getName();
		currentHealth = p.getCurrentHealth();
		maxHealth = p.getMaxHealth();
		gun = p.getGun() == null ? ".45" : p.getGun().getName();
		character = p.getCharacter().getName();
		table = p.getTable();
		hand = p.getHand();
		role = p.getRole().name();
		messages = p.getMessages();
	}
}
