package com.jtriemstra.bang.api.model.action;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jtriemstra.bang.api.model.card.Card;
import com.jtriemstra.bang.api.model.player.Player;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefenseRule {
	public static final String TYPE_MISSED = "missed";
	public static final String TYPE_BARREL = "barrel";
	public static final String TYPE_PASS = "pass";
	
	private String name;
	private Condition condition;
	private Consumer<List<BaseAction>> behavior;
	
	public interface Condition {
		public boolean apply(Player defender, Player attacker, Card attackCard);
	}
}
