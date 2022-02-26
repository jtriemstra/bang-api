package com.jtriemstra.bang.api.model.player;

public interface BangRule {
	boolean isValid();
	void execute();
	void reset();
}
