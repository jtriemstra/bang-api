package com.jtriemstra.bang.api.model.player;


public class BangRuleDefault implements BangRule {
	private boolean hasPlayedBang;
	private Player player;
	
	public BangRuleDefault(Player player) {
		this.player = player;
	}

	@Override
	public boolean isValid() {
		return !hasPlayedBang || (player.getGun() != null && player.getGun().getName().equals("Volcanic"));
	}

	@Override
	public void execute() {
		if (!hasPlayedBang || (player.getGun() != null && player.getGun().getName().equals("Volcanic"))) {
			hasPlayedBang = true;
		} else {
			throw new RuntimeException("You can only play the Bang card once per turn");
		}
	}

	@Override
	public void reset() {
		hasPlayedBang = false;
	}
}
