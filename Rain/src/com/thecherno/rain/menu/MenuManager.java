package com.thecherno.rain.menu;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

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
	private JFrame frame;

	private List<State> states = new ArrayList<State>();
	
	public enum States {
		LOADING,
		MAIN_MENU,
		INGAME
	}
	
	private boolean ready_state;
	private State current_state;
	private States current_id_state;
	
	public MenuManager(JFrame frame, Keyboard key) {
		this.frame = frame;
		this.key = key;
		ready_state = false;
		changeState(States.LOADING);
	}
	
	public States getIdState() {
		return current_id_state;
	}
	
	private void resizeFrame(int width, int height) {
		frame.setPreferredSize(new Dimension(width * Game.scale, height * Game.scale));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.pack();
	}
	
	public void changeState(States state) {
		ready_state = false;
		Game.getUIManager().clear();
		states.clear();
		switch (state) {
		case LOADING: {
			//resizeFrame(Game.width, Game.height);
			states.add(new Loading()); 
		} break;
		case MAIN_MENU:  {
			//resizeFrame(Game.width, Game.height);
			states.add(new MainMenu()); 
		} break;
		case INGAME: {
			//resizeFrame(Game.width + 80, Game.height);
			states.add(new InGame(key)); 
		} break;
		}
		current_id_state = state;
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
	
	public void render(Screen screeng, Screen screenall) {
		if (ready_state)
			if (current_id_state != States.INGAME)
				current_state.render(screenall);
			else
				current_state.render(screeng);
	}

	@Override
	public void onEvent(Event event) {
		if (ready_state)
			for (State state :states)
				state.onEvent(event);
	}
	
}
