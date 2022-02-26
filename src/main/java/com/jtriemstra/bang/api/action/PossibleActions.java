package com.jtriemstra.bang.api.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;


public class PossibleActions {
	private List<BaseAction> actions;

	public PossibleActions(BaseAction... a) {
		actions = new ArrayList<BaseAction>();
		for (BaseAction ba : a) {
			actions.add(ba);
		}
	}
	
	public boolean contains(String actionName) {
		for (BaseAction a : actions) {
			if (a.getName().equals(actionName)) {
				return true;
			}
		}
		
		return false;
	}

	public BaseAction getByName(String actionName) {
		for (BaseAction a : actions) {
			if (a.getName().equals(actionName)) {
				return a;
			}
		}
				
		// this accomodates a situation where a player is waiting, and a new action has been added to their stack (eg to defend or to start a new turn)
		if ("wait".equals(actionName)) {
			return new Waiting();
		}
		
		throw new RuntimeException("action " + actionName + " not currently available");
	}	
	
	public void removeAction(String name) {
		int matchingIndex = -1;
		for (int i=0; i<actions.size(); i++) {
			if (name.equals(actions.get(i).getName())) {
				matchingIndex = i;
			}
		}
						
		actions.remove(matchingIndex);
	}
	
	@Override
	public String toString() {
		String result = "";
		if (actions != null) {
			for (int i=0; i<actions.size(); i++) {
				if (i > 0) result += ";";
				result += actions.get(i).getName();
			}	
		}
		return result;
	}

	public void addAction(BaseAction action) {
		if (!actions.stream().anyMatch(a -> a.getName().equals(action.getName()))) {
			actions.add(action);	
		}
	}
}
