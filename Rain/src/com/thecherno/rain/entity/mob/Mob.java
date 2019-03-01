package com.thecherno.rain.entity.mob;

import com.thecherno.rain.entity.Entity;
import com.thecherno.rain.entity.projectile.Projectile;
import com.thecherno.rain.entity.projectile.WizardProjectile;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.util.Vector2i;

public abstract class Mob extends Entity {

	protected String name;
	protected boolean moving = false;
	protected boolean walking = false;
	
	protected int health;

	protected enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	protected Direction dir;
	
	protected Mob(String name) {
		this.name = name;
	}

	public void move(double xa, double ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}

		if (xa > 0) dir = Direction.RIGHT;
		if (xa < 0) dir = Direction.LEFT;
		if (ya > 0) dir = Direction.DOWN;
		if (ya < 0) dir = Direction.UP;

		for (int x = 0; x < Math.abs(xa); ++x) {
			if (!collision(abs(xa), ya)) {
				this.x += abs(xa);
			}
		}

		for (int y = 0; y < Math.abs(ya); ++y) {
			if (!collision(xa, abs(ya))) {
				this.y += abs(ya);
			}
		}
	}

	private int abs(double value) {
		if (value < 0) return -1;
		return 1;
	}

	public abstract void update();

	public abstract void render(Screen screen);

	protected void shoot(int x, int y, double dir) {
		Projectile p = new WizardProjectile(x, y, dir, name);
		level.add(p);
	}

	private boolean collision(double xa, double ya) {
		boolean solid = false;
		/*
		for (int c = 0; c < 4; ++c) {
			double xt = ((x + xa) - c % 2 * 15) / 16;
			double yt = ((y + ya) - c / 2 * 15) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			solid = level.getTile(ix, iy).solid();
		} */
		int ix = x >> 4;
		int iy = y >> 4;
		int xp =(int) Math.ceil(xa), yp = (int) Math.ceil(ya);
		solid = level.getTile(ix + xp, iy + yp).solid();
		return solid;
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2i getPosition() {
		return new Vector2i(x, y);
	}

}
