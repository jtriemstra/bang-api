package com.jtriemstra.bang.api.model;

import com.jtriemstra.bang.api.model.character.Character;

public class FakeCharacterDeck extends CharacterDeck {
	private static int count=0;
	@Override
	public Character draw() {
		Character[] x = new Character[] {
				new Character("DummySheriff", 4), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
		};
		
		return x[count++];
	}
	
	public Character[] getList(Game game) {
		return new Character[] {
				new Character("DummySheriff", 4), new Character("Dummy3", 3),new Character("Dummy", 4), new Character("Dummy", 4)
		};
	}
}
