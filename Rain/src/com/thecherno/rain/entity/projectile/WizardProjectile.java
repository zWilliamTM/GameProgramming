package com.thecherno.rain.entity.projectile;

import com.thecherno.rain.entity.spawner.ParticleSpawner;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;

public class WizardProjectile extends Projectile {

	public static final int FIRE_RATE = 10; // Higher is slower!

	public WizardProjectile(int x, int y, double dir, String sender) {
		super(x, y, dir, sender);
		range = 100;
		speed = 4;
		damage = 20;
		sprite = Sprite.rotate(Sprite.s.projectile_arrow, angle);
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}
	
	private int time = 0;

	public void update() {
		if (level.tileCollision((int) (x + nx), (int) (y + ny), 7, 5, 4) || // Tile Collision
			level.mobCollision((int) (x + nx), (int) (y + ny), 7, 5, 4, sender)) { // Player Collision
			level.add(new ParticleSpawner((int) x, (int) y, 44, 50, level));
			remove();
		}
		
		/* Animation of rotation.
		time++;
		if (time % 8 == 0) {
			sprite = Sprite.rotate(sprite, Math.PI / 30.0);
		}
		*/
		move();
	}

	protected void move() {
		x += nx;
		y += ny;
		if (distance() > range) remove();
	}

	private double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		return dist;
	}

	public void render(Screen screen) {
		screen.renderProjectile((int) x - 12, (int) y - 2, this);
	}

}
