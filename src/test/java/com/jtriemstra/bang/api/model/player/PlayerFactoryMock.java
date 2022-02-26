package com.jtriemstra.bang.api.model.player;

import org.mockito.Mockito;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.Role;
import com.jtriemstra.bang.api.model.character.Character;

@Component
@Primary
@Profile("integration")
public class PlayerFactoryMock implements PlayerFactory {

	@Override
	public Player create(String name, Role role, Character character, Game game) {
		return Mockito.spy(new Player(name, role, character, game));
	}

}
