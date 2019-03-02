package com.thecherno.rain.level.view;

import java.util.List;

import com.thecherno.rain.entity.mob.Mob;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;

public class MiniLevelView {
	
	private int iwidth, iheight;		// Pixels for show size
	public int width, height;			// View size
	private int xOffset, yOffset;	// Current player position
	public Sprite minimap = Sprite.s.minimap_1;
	public int[] pixels;
	
	public MiniLevelView() {
		iwidth = minimap.getWidth();
		iheight = minimap.getHeight();
		width = 240 >> 4;
		height = 168 >> 4;
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
	
	public void update(Mob main_player, List<Mob> players) {
		xOffset = (main_player.getX() - width / 2) >> 4;
		yOffset = (main_player.getY() - height / 2) >> 4;
		int x0 = xOffset;
		int x1 = x0 + width;
		int y0 = yOffset;
		int y1 = y0 + height;
		
		for (int y = y0; y < y1; ++y) {
			for (int x = x0; x < x1; ++x) {
				int mx = x, my = y;
				int color = (mx < 0 || mx >= iwidth || my < 0 || my >= iheight) ?  0 : minimap.pixels[mx + my * iwidth];
				for (Mob p : players) {
					if (p.getName().equals(main_player.getName())) {
						if (xOffset == x && yOffset == y) 
							color = 0xff0000;
					} else {
						if ((p.getX() >> 4) == x && (p.getY() >> 4) == y)
							color = 0x1b87e0;
					}
				}
				pixels[(x - x0) + (y - y0) * width] = color;
			}
		}
		
		/*
		for (int y = y0; y < y1; ++y) {
			for (int x = x0; x < x1; ++x) {
				int color = 0;
				if (x > iwidth || y > iheight) color = 0;
				else {
					color = minimap.pixels[x + y * iwidth];
				}
				for (Mob p : players) {
					if (p.getName().equals(main_player.getName())) {
						if (xOffset == x && yOffset == y) {
							if (p.getName().equals("WilliDev")) color = 0xffffff;
						}
					} 
					else if ((p.getX() >> 4) == x && (p.getY() >> 4) == y) {
						color = 0x1B87E0;
					}
					pixels[x + y * width] = color;
				}
			}
		} */
	}
	
	public void render(Screen screen) {
		screen.renderMinimap(0, 0, this);
	}
	
}
