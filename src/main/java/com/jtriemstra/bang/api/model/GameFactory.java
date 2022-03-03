package com.jtriemstra.bang.api.model;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.jtriemstra.bang.api.model.deck.CardDeckFactory;
import com.jtriemstra.bang.api.model.deck.CharacterDeckFactory;
import com.jtriemstra.bang.api.model.deck.RoleDeck;
import com.jtriemstra.bang.api.model.player.PlayerFactory;

@Component
public class GameFactory {
	
	@Autowired
	private NotificationService notifications;
	
	@Autowired
	private Supplier<UUID> idGenerator;
	
	@Autowired
	private PlayerFactory playerFactory;
	
	@Autowired
	private CardDeckFactory cardDeckFactory;
	
	@Autowired
	private CharacterDeckFactory characterDeckFactory;
	
	@Autowired
	private Supplier<RoleDeck> roleDeckFactory;
		
	public Game create(String gameName) {
		return new Game(gameName, idGenerator.get().toString(), characterDeckFactory.create(), cardDeckFactory.create(), notifications, playerFactory, roleDeckFactory.get());
	}

}
