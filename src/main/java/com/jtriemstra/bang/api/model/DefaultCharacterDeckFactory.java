package com.jtriemstra.bang.api.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultCharacterDeckFactory implements CharacterDeckFactory {
	
	@Value("${characterNames:}")
	private String names;
	
	public CharacterDeck create() {
		if (StringUtils.isEmpty(names)) {
			return new CharacterDeck();	
		}
		else {
			return new CharacterDeck(names);
		}
	}
}
