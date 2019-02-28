package com.thecherno.rain.menu.state;

import java.awt.Font;

import com.thecherno.rain.Game;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.ui.UIActionListener;
import com.thecherno.rain.graphics.ui.UIButton;
import com.thecherno.rain.graphics.ui.UILabel;
import com.thecherno.rain.graphics.ui.UIManager;
import com.thecherno.rain.graphics.ui.UIPanel;
import com.thecherno.rain.menu.MenuManager.States;
import com.thecherno.rain.util.Vector2i;
import static com.thecherno.rain.util.MathUtils.*;

public class MainMenu extends State implements EventListener {
	
	// Components
	private Sprite background;
	private UIManager ui;
	
	public MainMenu() {
		identifier = States.MAIN_MENU;
	}
	
	@Override
	public void init() {
		int w = Game.all_width;
		int h = Game.all_height;
		int sx = 200;
		int sy = 300;
		
		// Background
		background = new Sprite(w, h, 0x00aabb);
		
		// UI Component
		ui = Game.getUIManager();
		UIPanel panel = (UIPanel) new UIPanel(
				new Vector2i(w / 2 - (sx / 2), h / 2 - (sy / 2)), 
				new Vector2i(sx, sy))
				.setColor(0x4f4f4f);
		ui.addPanel(panel);

		int wtext = calculateTextWidth("Rain".length(), 18);
		UILabel title = new UILabel(new Vector2i(sx / 2 - (wtext / 2),  40), "Rain");
		title.setColor(0xffffff);
		title.setFont(new Font("Verdana", Font.PLAIN, 18));
		panel.addComponent(title);
		
		UIButton button = new UIButton(new Vector2i(sx / 2 - (100 / 2), 90 - 30 / 2),
				new Vector2i(100, 30), new UIActionListener() {
					public void perform() {
						actionButton = ActionButtonType.START_GAME;
						finish();
					}
				});
		button.setText("Start");
		panel.addComponent(button);
		
		UIButton button1 = new UIButton(new Vector2i(sx / 2 - (100 / 2), 150 - 30 / 2),
				new Vector2i(100, 30), new UIActionListener() {
					public void perform() {
						actionButton = ActionButtonType.EXIT_GAME;
						finish();
					}
				});
		button1.setText("Exit");
		panel.addComponent(button1);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Screen screen) {
		screen.renderSprite(0, 0, background, false);
	}
	
	@Override
	public void finish() {
		finished = true;
	}

	@Override
	public void onEvent(Event event) {
	}

}
