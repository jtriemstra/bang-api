package com.jtriemstra.bang.api.model;

import java.util.HashMap;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class GameList {
	private HashMap<String, Game> games = new HashMap<>();
	private HashMap<String, Game> gamesById = new HashMap<>();
	
	public void add(String name, Game game) {
		games.put(name, game);
		gamesById.put(game.getId(), game);
	}
	
	public Set<String> list(){
		return games.keySet();
	}
	
	public Game get(String name) {
		return games.get(name);
	}
	
	public Game getById(String id) {
		return gamesById.get(id);
	}
}
