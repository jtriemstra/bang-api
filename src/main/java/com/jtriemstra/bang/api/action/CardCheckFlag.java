package com.jtriemstra.bang.api.action;

import java.util.function.Function;

import com.jtriemstra.bang.api.model.Game;
import com.jtriemstra.bang.api.model.card.Card;

public interface CardCheckFlag {
	default void setCardBehavior(Function<Game, Card> f) {}
}
