package com.jtriemstra.bang.api.model.player;

import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.character.Character;

@Component
public class PlayerFactoryDefault implements PlayerFactory {

	@Override
	public Player create(String name, Role role, Character character, Game game) {
		return new Player(name, role, character, game);
	}

}
