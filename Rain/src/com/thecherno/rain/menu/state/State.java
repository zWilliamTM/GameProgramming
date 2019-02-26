package com.thecherno.rain.menu.state;

import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.menu.MenuManager.States;

public abstract class State implements EventListener {
	
	protected States identifier;
	
	protected boolean finished = false;
	
	public enum ActionButtonType {
		// MainMenu
		START_GAME,
		EXIT_GAME,
		
		// Generic
		NONE
	}
	
	public ActionButtonType actionButton = ActionButtonType.NONE;
	
	public State() {
	}
	
	public abstract void init();
	
	public abstract void finish();
	
	public abstract void update();
	
	public abstract void render(Screen screen);
	
	public boolean isFinished() {
		return finished;
	}
	
	public States getId() {
		return identifier;
	}
	
}
