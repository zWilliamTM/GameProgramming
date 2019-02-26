package com.thecherno.rain.entity.mob.races;

import java.util.List;

import com.thecherno.rain.entity.Entity;
import com.thecherno.rain.entity.mob.Mob;
import com.thecherno.rain.graphics.AnimatedSprite;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.SpriteSheet;
import com.thecherno.rain.util.Vector2i;

public class Shooter extends Mob {

	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.ss.dummy_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.ss.dummy_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.ss.dummy_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.ss.dummy_right, 32, 32, 3);

	private AnimatedSprite animSprite = down;
	private int time = 0, delay_shoot = 15;
	private int xa = 0, ya = 0;

	private Entity rand = null;

	public Shooter(int x, int y) {
		super("Shooter");
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.s.dummy;
	}

	public void update() {
		time++;
		if (time % (random.nextInt(50) + 30) == 0) {
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			if (random.nextInt(4) == 0) {
				xa = 0;
				ya = 0;
			}
		}
		if (walking) animSprite.update();
		else animSprite.setFrame(0);
		if (ya < 0) {
			animSprite = up;
			dir = Direction.UP;
		} else if (ya > 0) {
			animSprite = down;
			dir = Direction.DOWN;
		}
		if (xa < 0) {
			animSprite = left;
			dir = Direction.LEFT;
		} else if (xa > 0) {
			animSprite = right;
			dir = Direction.RIGHT;
		}
		if (xa != 0 || ya != 0) {
			//move(xa, ya);
			walking = true;
		} else {
			walking = false;
		}

		//shootRandom();
		//shootClosest();
	}

	private void shootRandom() {
		List<Entity> entities = level.getEntities(this, 1);
		entities.add(level.getClientPlayer());
		if (time % (1 + random.nextInt(91)) == 0) {
			int index = random.nextInt(entities.size());
			rand = entities.get(index);
		}

		
		if (delay_shoot == 0) {
			if (rand != null) {
				double dx = rand.getX() - x;
				double dy = rand.getY() - y;
				double dir = Math.atan2(dy, dx);
				shoot(x, y, dir);
			}
			delay_shoot = 15;
		}
		delay_shoot--;
	}

	private void shootClosest() {
		List<Entity> entities = level.getEntities(this, 1);
		entities.add(level.getClientPlayer());

		double min = 0;
		Entity closest = null;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i(e.getX(), e.getY()));
			if (i == 0 || distance < min) {
				min = distance;
				closest = e;
			}
		}

		if (closest != null) {
			double dx = closest.getX() - x;
			double dy = closest.getY() - y;
			double dir = Math.atan2(dy, dx);
			shoot(x, y, dir);
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.drawRect(x - 16, y - 16, 32, 32, 0xffffaaff, true);
		screen.renderMob(x - 16, y - 16, this);
	}

}
