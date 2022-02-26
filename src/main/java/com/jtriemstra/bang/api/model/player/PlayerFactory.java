package com.jtriemstra.bang.api.model.player;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.character.Character;

public interface PlayerFactory {
	Player create(String name, Role role, Character character, Game game);
}
