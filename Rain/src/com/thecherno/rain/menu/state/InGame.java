package com.thecherno.rain.menu.state;

import java.util.ArrayList;
import java.util.List;

import com.thecherno.rain.entity.mob.Player;
import com.thecherno.rain.entity.mob.races.Shooter;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.layers.Layer;
import com.thecherno.rain.input.Keyboard;
import com.thecherno.rain.level.Level;
import com.thecherno.rain.level.TileCoordinate;
import com.thecherno.rain.menu.MenuManager.States;

public class InGame extends State implements EventListener {

	private List<Layer> stackLayer = new ArrayList<Layer>();
	
	private Keyboard key;
	
	private Level level;
	private Player player;
	
	public InGame(Keyboard key) {
		identifier = States.INGAME;
		this.key = key;
	}
	
	private void addLayer(Layer layer) {
		stackLayer.add(layer);
	}
	
	@Override
	public void init() {
		level = Level.spawn;
		
		TileCoordinate playerSpawn = new TileCoordinate(19, 42);
		player = new Player("WilliDev", playerSpawn.x(), playerSpawn.y(), key);
		level.addPlayer(player);
		//level.addPlayer(new NetPlayer());
		level.addPlayer(new Shooter(20, 55));
		level.addPlayer(new Shooter(23, 60));
		
		addLayer(level);
	}
	
	@Override
	public void update() {
		for (Layer l : stackLayer)
			l.update();
	}

	@Override
	public void render(Screen screen) {
		int xScroll = player.getX() - screen.width / 2;
		int yScroll = player.getY() - screen.height / 2;
		level.setScroll(xScroll, yScroll);
		
		for (Layer l : stackLayer)
			l.render(screen);
	}
	
	@Override
	public void finish() {
	}

	@Override
	public void onEvent(Event event) {
		for (Layer l : stackLayer)
			l.onEvent(event);
	}

}
