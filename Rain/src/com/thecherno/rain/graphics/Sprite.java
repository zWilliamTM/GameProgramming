package com.thecherno.rain.graphics;

public class Sprite {

	public int SIZE;
	private int x, y;
	private int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;

	public Sprite grass;
	public Sprite flower;
	public Sprite rock;
	public Sprite voidSprite;

	//Spawn Level Sprites here:
	public Sprite spawn_grass;
	public Sprite spawn_hedge;
	public Sprite spawn_water;
	public Sprite spawn_wall1;
	public Sprite spawn_wall2;
	public Sprite spawn_floor;

	//Player Sprites here:
	public Sprite player_forward;
	public Sprite player_back;
	public Sprite player_side;

	public Sprite player_forward_1;
	public Sprite player_forward_2;

	public Sprite player_side_1;
	public Sprite player_side_2;

	public Sprite player_back_1;
	public Sprite player_back_2;

	public Sprite dummy;

	//Projectile Sprites here:
	public Sprite projectile_wizard;
	public Sprite projectile_arrow;

	// Particles
	public Sprite particle_normal;
	public Sprite square;
	
	// Mini map
	public Sprite minimap_1;
	
	// Global var for statics initialization.
	public static Sprite s = new Sprite();
	
	private Sprite() {
	}

	protected Sprite(SpriteSheet sheet, int width, int height) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();
	}

	public Sprite(int width, int height, int colour) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		setColour(colour);
	}

	public Sprite(int size, int colour) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColour(colour);
	}

	public Sprite(int[] pixels, int width, int height) {
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
	
	// for statics initializations
	public void init() {
		grass = new Sprite(16, 0, 5, SpriteSheet.ss.tiles);
		flower = new Sprite(16, 1, 0, SpriteSheet.ss.tiles);
		rock = new Sprite(16, 2, 0, SpriteSheet.ss.tiles);
		voidSprite = new Sprite(16, 0x1B87E0);

		//Spawn Level Sprites here:
		spawn_grass = new Sprite(16, 0, 0, SpriteSheet.ss.spawn_level);
		spawn_hedge = new Sprite(16, 1, 0, SpriteSheet.ss.spawn_level);
		spawn_water = new Sprite(16, 2, 0, SpriteSheet.ss.spawn_level);
		spawn_wall1 = new Sprite(16, 0, 1, SpriteSheet.ss.spawn_level);
		spawn_wall2 = new Sprite(16, 0, 2, SpriteSheet.ss.spawn_level);
		spawn_floor = new Sprite(16, 1, 1, SpriteSheet.ss.spawn_level);

		//Player Sprites here:
		player_forward = new Sprite(32, 0, 5, SpriteSheet.ss.tiles);
		player_back = new Sprite(32, 2, 5, SpriteSheet.ss.tiles);
		player_side = new Sprite(32, 1, 5, SpriteSheet.ss.tiles);

		player_forward_1 = new Sprite(32, 0, 6, SpriteSheet.ss.tiles);
		player_forward_2 = new Sprite(32, 0, 7, SpriteSheet.ss.tiles);

		player_side_1 = new Sprite(32, 1, 6, SpriteSheet.ss.tiles);
		player_side_2 = new Sprite(32, 1, 7, SpriteSheet.ss.tiles);

		player_back_1 = new Sprite(32, 2, 6, SpriteSheet.ss.tiles);
		player_back_2 = new Sprite(32, 2, 7, SpriteSheet.ss.tiles);

		dummy = new Sprite(32, 0, 0, SpriteSheet.ss.dummy_down);

		//Projectile Sprites here:
		projectile_wizard = new Sprite(16, 0, 0, SpriteSheet.ss.projectile_wizard);
		projectile_arrow = new Sprite(16, 1, 0, SpriteSheet.ss.projectile_wizard);

		// Particles
		particle_normal = new Sprite(3, 0xAAAAAA);
		square = new Sprite(2, 0xFF0000);
		
		// Level minimap
		minimap_1 = new Sprite(SpriteSheet.ss.minimap_default.pixels, 40, 75);
	}
	
	public static Sprite rotate(Sprite sprite, double angle) {
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
	}
	
	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];
		
		double nx_x = rot_x(-angle, 1.0, 0.0);
		double nx_y = rot_y(-angle, 1.0, 0.0);
		double ny_x = rot_x(-angle, 0.0, 1.0);
		double ny_y = rot_y(-angle, 0.0, 1.0);
		
		double x0 = rot_x(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rot_y(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if (xx < 0 || xx >= width || yy < 0 || yy >= height) col = 0xffff00ff;
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}			
			x0 += ny_x;
			y0 += ny_y;
		}
		
		return result;
	}
	
	private static double rot_x(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos + y * -sin;
	}

	private static double rot_y(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}


	public static Sprite[] split(SpriteSheet sheet) {
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT);
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT];
		for (int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) {
			for (int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++) {

				for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
					for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
						int xo = x + xp * sheet.SPRITE_WIDTH;
						int yo = y + yp * sheet.SPRITE_HEIGHT;
						pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[xo + yo * sheet.getWidth()];
					}
				}

				sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
			}
		}

		return sprites;
	}

	public Sprite(int[] pixels, int size) {
		SIZE = width = height = size;
		this.pixels = pixels;
	}

	private void setColour(int colour) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = colour;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void load() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
			}
		}
	}
}
