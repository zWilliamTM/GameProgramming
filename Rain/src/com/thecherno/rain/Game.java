package com.thecherno.rain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.thecherno.rain.entity.mob.Player;
import com.thecherno.rain.entity.mob.races.Shooter;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.events.EventListener;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.layers.Layer;
import com.thecherno.rain.graphics.ui.UIManager;
import com.thecherno.rain.input.Keyboard;
import com.thecherno.rain.input.Mouse;
import com.thecherno.rain.level.Level;
import com.thecherno.rain.level.TileCoordinate;
import com.thecherno.rain.net.Client;
import com.thecherno.rain.net.player.NetPlayer;
import com.thecherno.raincloud.serialization.RCDatabase;
import com.thecherno.raincloud.serialization.RCField;
import com.thecherno.raincloud.serialization.RCObject;

public class Game extends Canvas implements Runnable, EventListener {
	private static final long serialVersionUID = 1L;

	private static int width = 300 - 80;
	private static int height = 168;
	private static int scale = 3;
	public static String title = "Rain";

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Level level;
	private Player player;
	private boolean running = false;
	
	private static UIManager uiManager;

	private Screen screen;
	private BufferedImage image;
	private int[] pixels;
	
	private List<Layer> layerStack = new ArrayList<Layer>();

	public Game() {
		setSize();

		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();
		
		// TODO: Connect to server here!
		Client client = new Client("localhost", 8192);
		if (!client.connect()) {
			// TODO: We didn't connect
		}
		
		RCDatabase db = RCDatabase.DeserializeFromFile("res/data/screen.bin");
		// client.send(db);
		
		level = Level.spawn;
		addLayer(level);
		TileCoordinate playerSpawn = new TileCoordinate(19, 42);
		player = new Player("WilliDev", playerSpawn.x(), playerSpawn.y(), key);
		level.addPlayer(player);
		//level.addPlayer(new NetPlayer());
		level.addPlayer(new Shooter(20, 55));
		level.addPlayer(new Shooter(23, 60));
		addKeyListener(key);

		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		save();
	}
	
	private void setSize() {
		RCDatabase db = RCDatabase.DeserializeFromFile("res/data/screen.bin");
		if (db != null) {
			RCObject obj = db.findObject("Resolution");
			width = obj.findField("width").getInt();
			height = obj.findField("height").getInt();
			scale = obj.findField("scale").getInt();
		}
		
		Dimension size = new Dimension(width * scale + 80 * 3, height * scale);
		setPreferredSize(size);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	private void save() {
		RCDatabase db = new RCDatabase("Screen");
		
		RCObject obj = new RCObject("Resolution");
		obj.addField(RCField.Integer("width", width));
		obj.addField(RCField.Integer("height", height));
		obj.addField(RCField.Integer("scale", scale));
		db.addObject(obj);
		
		db.serializeToFile("res/data/screen.bin");
	}

	public static int getWindowWidth() {
		return width * scale;
	}

	public static int getWindowHeight() {
		return height * scale;
	}
	
	public static UIManager getUIManager() {
		return uiManager;
	}
	
	public void addLayer(Layer layer) {
		layerStack.add(layer);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println(updates + " ups, " + frames + " fps");
				frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void onEvent(Event event) {
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).onEvent(event);
		}
	}

	public void update() {
		key.update();
		uiManager.update();
		
		// Update layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).update();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		int xScroll = player.getX() - screen.width / 2;
		int yScroll = player.getY() - screen.height / 2;
		level.setScroll(xScroll, yScroll);

		// Render layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).render(screen);
		}
		
		// font.render(50, 50, -3, "Hey what's up\nguys, My name is\nThe Cherno!", screen);
		// screen.renderSheet(40, 40, SpriteSheet.player_down, false);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(0xff00ff));
		g.fillRect(0,  0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		uiManager.render(g);
		// g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64);
		// if (Mouse.getButton() != -1) g.drawString("Button: " + Mouse.getButton(), 80, 80);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
