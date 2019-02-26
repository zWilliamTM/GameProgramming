package com.thecherno.rain.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	public int SIZE;
	public int SPRITE_WIDTH, SPRITE_HEIGHT;
	private int width, height;
	public int[] pixels;

	public SpriteSheet tiles;
	public SpriteSheet spawn_level;
	public SpriteSheet projectile_wizard;

	public SpriteSheet player;
	public SpriteSheet player_down;
	public SpriteSheet player_up;
	public SpriteSheet player_left;
	public SpriteSheet player_right;

	public SpriteSheet dummy;
	public SpriteSheet dummy_down;
	public SpriteSheet dummy_up;
	public SpriteSheet dummy_left;
	public SpriteSheet dummy_right;
	
	public static SpriteSheet ss = new SpriteSheet();

	private Sprite[] sprites;

	private SpriteSheet() {
	}
	
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		if (width == height) SIZE = width;
		else SIZE = -1;
		SPRITE_WIDTH = w;
		SPRITE_HEIGHT = h;
		pixels = new int[w * h];
		for (int y0 = 0; y0 < h; y0++) {
			int yp = yy + y0;
			for (int x0 = 0; x0 < w; x0++) {
				int xp = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.SPRITE_WIDTH];
			}
		}
		int frame = 0;
		sprites = new Sprite[width * height];
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {
				int[] spritePixels = new int[spriteSize * spriteSize];
				for (int y0 = 0; y0 < spriteSize; y0++) {
					for (int x0 = 0; x0 < spriteSize; x0++) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize) * SPRITE_WIDTH];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}

	public SpriteSheet(String path, int size) {
		this.path = path;
		SIZE = size;
		SPRITE_WIDTH = size;
		SPRITE_HEIGHT = size;
		load();
	}

	public SpriteSheet(String path, int width, int height) {
		this.path = path;
		SIZE = -1;
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
		pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
		load();
	}
	
	public void init() {
		tiles 				= new SpriteSheet("/textures/sheets/spritesheet.png", 256);
		spawn_level 		= new SpriteSheet("/textures/sheets/spawn_lvl.png", 48);
		projectile_wizard 	= new SpriteSheet("/textures/sheets/projectiles/wizard.png", 48);

		player 			= new SpriteSheet("/textures/sheets/player_sheet.png", 128, 96);
		player_down 	= new SpriteSheet(player, 0, 0, 1, 3, 32);
		player_up 		= new SpriteSheet(player, 1, 0, 1, 3, 32);
		player_left 	= new SpriteSheet(player, 2, 0, 1, 3, 32);
		player_right 	= new SpriteSheet(player, 3, 0, 1, 3, 32);

		dummy 		= new SpriteSheet("/textures/sheets/king_cherno.png", 128, 96);
		dummy_down 	= new SpriteSheet(dummy, 0, 0, 1, 3, 32);
		dummy_up 	= new SpriteSheet(dummy, 1, 0, 1, 3, 32);
		dummy_left 	= new SpriteSheet(dummy, 2, 0, 1, 3, 32);
		dummy_right = new SpriteSheet(dummy, 3, 0, 1, 3, 32);
	}

	public Sprite[] getSprites() {
		return sprites;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}

	private void load() {
		try {
			System.out.print("Trying to load: " + path + "...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println(" succeeded!");
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(" failed!");
		}

	}

}
