package com.thecherno.rain.level.view;

import java.util.ArrayList;
import java.util.List;

import com.thecherno.rain.entity.mob.Mob;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.util.Vector2i;

public class MiniLevelView {
	
	private int width, height;		// Pixels for show size
	private int xOffset, yOffset;	// Current player position
	public Sprite minimap = Sprite.s.minimap_1;
	public int[] pixels;
	
	public MiniLevelView() {
		width = minimap.getWidth();
		height = minimap.getHeight();
		pixels = new int[width * height];
	}
	
	MiniLevelView(int width, int height) {
	}
	
	MiniLevelView(int width, int height, Sprite minimap_image) {
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void update(List<Mob> players) {
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int color = minimap.pixels[x + y * width];;
				for (Mob p : players) {
					int xa = (p.getX() >> 4), ya = (p.getY() >> 4);
					if (xa == x && ya == y) {
						if (p.getName().equals("WilliDev")) color = 0xffffff;
						else if (p.getName().equals("Shooter")) color = 0x1B87E0;
					}
					pixels[x + y * width] = color;
				}
			}
		}
	}
	
	public void render(Screen screen) {
		screen.renderMinimap(0, 0, this);
	}
	
}
