package com.thecherno.rain.level.tile;

import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.level.tile.spawn_level.SpawnFloorTile;
import com.thecherno.rain.level.tile.spawn_level.SpawnGrassTile;
import com.thecherno.rain.level.tile.spawn_level.SpawnHedgeTile;
import com.thecherno.rain.level.tile.spawn_level.SpawnWallTile;
import com.thecherno.rain.level.tile.spawn_level.SpawnWaterTile;

public class Tile {

	public Sprite sprite;

	public Tile grass;
	public Tile flower;
	public Tile rock;
	public Tile voidTile;

	public Tile spawn_grass;
	public Tile spawn_hedge;
	public Tile spawn_water;
	public Tile spawn_wall1;
	public Tile spawn_wall2;
	public Tile spawn_floor;

	public static final int col_spawn_grass = 0xff00ff00;
	public static final int col_spawn_hedge = 0; //unused
	public static final int col_spawn_water = 0; //unused
	public static final int col_spawn_wall1 = 0xff808080;
	public static final int col_spawn_wall2 = 0xff303030;
	public static final int col_spawn_floor = 0xff724715;
	public static final int col_spawn = 0xff38EEFF;
	
	public static Tile t = new Tile();
	
	private Tile() {
	}

	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void init() {
		grass = new GrassTile(Sprite.s.grass);
		flower = new FlowerTile(Sprite.s.flower);
		rock = new RockTile(Sprite.s.rock);
		voidTile = new VoidTile(Sprite.s.voidSprite);

		spawn_grass = new SpawnGrassTile(Sprite.s.spawn_grass);
		spawn_hedge = new SpawnHedgeTile(Sprite.s.spawn_hedge);
		spawn_water = new SpawnWaterTile(Sprite.s.spawn_water);
		spawn_wall1 = new SpawnWallTile(Sprite.s.spawn_wall1);
		spawn_wall2 = new SpawnWallTile(Sprite.s.spawn_wall2);
		spawn_floor = new SpawnFloorTile(Sprite.s.spawn_floor);
	}

	public void render(int x, int y, Screen screen) {
	}

	public boolean solid() {
		return false;
	}

}
