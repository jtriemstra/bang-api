package com.jtriemstra.bang.api.model.state;

import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.card.Gun;
import com.jtriemstra.bang.api.model.character.Character;
import com.jtriemstra.bang.api.model.player.Player;
import com.jtriemstra.bang.api.model.player.Table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OtherPlayerState {
	private String name;
	private int currentHealth, maxHealth;
	private String gun;
	private String character;
	private Table table;
	private String sheriff;

	public OtherPlayerState(Player p) {
		name = p.getName();
		currentHealth = p.getCurrentHealth();
		maxHealth = p.getMaxHealth();
		gun = p.getGun() == null ? ".45" : p.getGun().getName();
		character = p.getCharacter().getName();
		table = p.getTable();
		sheriff = (p.getRole() == Role.SHERIFF ? "Yes" : "No");
	}
}
