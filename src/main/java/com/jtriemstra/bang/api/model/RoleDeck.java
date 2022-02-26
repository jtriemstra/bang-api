package com.jtriemstra.bang.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.util.Assert;

public class RoleDeck {
	
	private List<Role> roles;
	private Random randomizer = new Random();
	
	public void initialize(int playerCount) {
		Assert.isTrue(playerCount >= 4 && playerCount <= 7, "invalid number of players");
		
		switch (playerCount) {
		case 4:
			roles = new ArrayList<Role>(Arrays.asList(new Role[] {Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW})); break;
		case 5:
			roles = new ArrayList<Role>(Arrays.asList(new Role[] {Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW, Role.DEPUTY})); break;
		case 6:
			roles = new ArrayList<Role>(Arrays.asList(new Role[] {Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW, Role.DEPUTY, Role.OUTLAW})); break;
		case 7:
			roles = new ArrayList<Role>(Arrays.asList(new Role[] {Role.SHERIFF, Role.RENEGADE, Role.OUTLAW, Role.OUTLAW, Role.DEPUTY, Role.OUTLAW, Role.DEPUTY})); break;
		default:
			throw new RuntimeException("This is unreachable code that the compiler can't see");
		}
	}
	
	public Role draw() {
		return roles.remove(randomizer.nextInt(roles.size()));
	}
}
