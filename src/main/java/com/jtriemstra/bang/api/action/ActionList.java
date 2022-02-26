package com.jtriemstra.bang.api.action;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	private List<PossibleActions> actions = new ArrayList<>();

	public BaseAction getCurrentByName(String actionName) {
		return actions.get(actions.size() - 1).getByName(actionName);
	}

	public void push(BaseAction... a) {
		actions.add(new PossibleActions(a));
	}

	public PossibleActions getNext() {
		return actions.get(actions.size() - 1);
	}

	public PossibleActions pop() {
		return actions.remove(actions.size() - 1);
	}

	public int size() {
		return actions.size();
	}

}
