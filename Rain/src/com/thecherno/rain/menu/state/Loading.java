package com.thecherno.rain.menu.state;

import com.thecherno.rain.Game;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.SpriteSheet;
import com.thecherno.rain.graphics.ui.UIProgressBar;
import com.thecherno.rain.level.tile.Tile;
import com.thecherno.rain.menu.MenuManager.States;
import com.thecherno.rain.util.Vector2i;

public class Loading extends State implements Runnable, EventListener {

	public enum LoadingBackground {
		BEGIN_LOAD,		// Carga lo necesario para los menus...
		INROOM_LOAD 	// Carga lo necesario para mostrar salas...
	}
	
	private final int P_STEP = 3;
	private final int P_WIDTH = 300;
	private int current_step = 0;
	
	// Components for screen Loading
	private Sprite background;
	private UIProgressBar progressBar;
	
	public Loading() {
		identifier = States.LOADING;
	}
	
	@Override
	public void init() {
		// Loading background
		int w = Game.getWindowWidth();
		int h = Game.getWindowHeight();
		background = new Sprite(w, h, 0x1B87E0); // test
		progressBar = new UIProgressBar(new Vector2i(w / 2 - (P_WIDTH / 2), h / 2 - 20),
										new Vector2i(P_WIDTH, 40));
		progressBar.setColor(0xffffff);
		progressBar.setForegroundColor(0xffff00);
		
		Game.getUIManager().addComponent(progressBar);
		
		Thread th = new Thread(this, "Loading");
		th.start();
	}
	
	private synchronized void updateProgress() {
		// calculate
		if (current_step >= P_STEP)
			progressBar.setProgress(1.0); // finalized
		else
			progressBar.setProgress(current_step * P_STEP / 10.0);
	}
	
	@Override
	public void run() {
		try {
			// Step 1
			Thread.sleep(100);
			SpriteSheet.ss.init();
			updateProgress();
			++current_step;
			// Step 2
			Thread.sleep(100);
			Sprite.s.init();
			updateProgress();
			++current_step;
			// Step 3 
			Thread.sleep(100);
			Tile.t.init();
			updateProgress();
			++current_step;
			
			Thread.sleep(100);
			// finished
			updateProgress();
			Thread.sleep(500);
			finish();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update() {
	}

	@Override
	public void render(Screen screen) {
		screen.renderSprite(0, 0, background, true);
	}
	
	@Override
	public void finish() {
		finished = true;
	}

	@Override
	public void onEvent(Event event) {
	}

}
