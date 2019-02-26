package com.thecherno.rain.menu;

import java.util.ArrayList;
import java.util.List;

import com.thecherno.rain.Game;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.layers.Layer;
import com.thecherno.rain.input.Keyboard;
import com.thecherno.rain.menu.state.InGame;
import com.thecherno.rain.menu.state.Loading;
import com.thecherno.rain.menu.state.MainMenu;
import com.thecherno.rain.menu.state.State;
import com.thecherno.rain.menu.state.State.ActionButtonType;;

public class MenuManager implements EventListener {
	
	
	private Keyboard key;

	private List<State> states = new ArrayList<State>();
	
	public enum States {
		LOADING,
		MAIN_MENU,
		INGAME
	}
	
	private boolean ready_state;
	private State current_state;
	
	public MenuManager(Keyboard key) {
		this.key = key;
		ready_state = false;
		changeState(States.LOADING);
	}
	
	public void changeState(States state) {
		ready_state = false;
		Game.getUIManager().clear();
		states.clear();
		switch (state) {
		case LOADING: {
			states.add(new Loading()); 
		} break;
		case MAIN_MENU:  {
			states.add(new MainMenu()); 
		} break;
		case INGAME: {
			states.add(new InGame(key)); 
		} break;
		}
		loadState(state);
		ready_state = true;
	}
	
	private void loadState(States state) {
		current_state = states.get(0);
		current_state.init();
	}
	
	private void checkStates() {
		for (State state: states) {
			if (state.isFinished()) {
				switch (state.getId()) {
				case LOADING: changeState(States.MAIN_MENU); break;
				case MAIN_MENU: {
					ActionButtonType ab = state.actionButton;
					switch (ab) {
					case START_GAME: changeState(States.INGAME); break;
					case EXIT_GAME: System.exit(0); break;
					}
				} break;
				case INGAME: break;
				}
			}
		}
	}
	
	public void update() {
		if (ready_state)
			for (State state : states) {
				state.update();
			}
		
		checkStates();
	}
	
	public void render(Screen screen) {
		if (ready_state)
			current_state.render(screen);
	}

	@Override
	public void onEvent(Event event) {
		if (ready_state)
			for (State state :states)
				state.onEvent(event);
	}
	
}
