package com.thecherno.rain.level;

import java.util.ArrayList
;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thecherno.rain.entity.Entity;
import com.thecherno.rain.entity.mob.Mob;
import com.thecherno.rain.entity.mob.Player;
import com.thecherno.rain.entity.particle.Particle;
import com.thecherno.rain.entity.projectile.Projectile;
import com.thecherno.rain.events.Event;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.layers.Layer;
import com.thecherno.rain.level.tile.Tile;
import com.thecherno.rain.level.view.MiniLevelView;
import com.thecherno.rain.util.Vector2i;

public class Level extends Layer {

	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	protected int tile_size;
	
	private int xScroll, yScroll;
	
	private MiniLevelView mlv = new MiniLevelView();

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Particle> particles = new ArrayList<Particle>();

	public List<Mob> players = new ArrayList<Mob>();

	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;
			if (n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};

	public static Level spawn = new SpawnLevel("/levels/spawn.png");

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
		generateLevel();
	}

	public Level(String path) {
		loadLevel(path);
		initNpc();
		generateLevel();
	}

	protected void generateLevel() {
		for (int y = 0; y < 64; y++) {
			for (int x = 0; x < 64; x++) {
				getTile(x, y);
			}
		}
		tile_size = 16;
	}

	protected void loadLevel(String path) {
	}
	
	protected void initNpc() {
	}

	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).update();
		}
		
		mlv.update(players);
		
		remove();
	}
	
	public void onEvent(Event event) {
		getClientPlayer().onEvent(event);	
	}

	private void remove() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isRemoved()) entities.remove(i);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isRemoved()) projectiles.remove(i);
		}
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).isRemoved()) particles.remove(i);
		}
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isRemoved()) players.remove(i);
		}
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	private void time() {
	}

	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = (x - c % 2 * size + xOffset) >> 4;
			int yt = (y - c / 2 * size + yOffset) >> 4;
			if (getTile(xt, yt).solid()) solid = true;
		}
		return solid;
	}
	
	public boolean mobCollision(int x, int y, int size, int xOffset, int yOffset, String instace) {
		boolean collider;
		int left_t = x;
		int right_t = left_t + size;
		int top_t = y;
		int bottom_t = top_t + size;
			//int xt = (x - c % 2 * size + xOffset) >> 4;
			//int yt = (y - c / 2 * size + yOffset) >> 4;
		List<Mob> m =  getPlayers();
		for (Mob p : m) {
			if (instace == p.getName()) continue;
			int left = p.getX() - 8;
			int right = left + 32 - 2; // SIZE SPRITE
			int top = p.getY() - 16 - 4;
			int bottom = top + 32 + 4;
			collider = ((left_t > left && right_t < right) && 	// collision axis X
						(top_t > top && bottom_t < bottom));	// collision axis Y
			if (collider) return true;
		}
		return false;
	}
	
	public void setScroll(int xScroll, int yScroll) {
		this.xScroll = xScroll;
		this.yScroll = yScroll;
	}

	public void render(Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(screen);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(screen);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render(screen);
		}
		
		mlv.render(screen);
	}

	public void add(Entity e) {
		e.init(this);
		if (e instanceof Particle) {
			particles.add((Particle) e);
		} else if (e instanceof Projectile) {
			projectiles.add((Projectile) e);
		} else if (e instanceof Player) {
			players.add((Player) e);
		} else {
			entities.add(e);
		}
	}
	
	public void addPlayer(Mob player) {
		player.init(this);
		players.add(player);
	}

	public List<Mob> getPlayers() {
		return players;
	}

	public Mob getPlayerAt(int index) {
		return players.get(index);
	}

	public Player getClientPlayer() {
		return (Player) players.get(0);
	}

	public List<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, getDistance(start, goal));
		openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.solid()) continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= node.gCost) continue;
				if (!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}
	
	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
		}
		return false;
	}

	private double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.getX() - goal.getX();
		double dy = tile.getY() - goal.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public List<Entity> getEntities(Entity e, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.equals(e)) continue;
			int x = entity.getX();
			int y = entity.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(entity);
		}
		return result;
	}

	public List<Mob> getPlayers(Entity e, int radius) {
		List<Mob> result = new ArrayList<Mob>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < players.size(); i++) {
			Mob player = players.get(i);
			int x = player.getX();
			int y = player.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(player);
		}
		return result;
	}

	// Grass = 0xFF00FF00
	// Flower = 0xFFFFFF00
	// Rock = 0xFF7F7F00
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return Tile.t.voidTile;
		if (tiles[x + y * width] == Tile.col_spawn_floor) return Tile.t.spawn_floor;
		if (tiles[x + y * width] == Tile.col_spawn_grass) return Tile.t.spawn_grass;
		if (tiles[x + y * width] == Tile.col_spawn_hedge) return Tile.t.spawn_hedge;
		if (tiles[x + y * width] == Tile.col_spawn_wall1) return Tile.t.spawn_wall1;
		if (tiles[x + y * width] == Tile.col_spawn_wall2) return Tile.t.spawn_wall2;
		if (tiles[x + y * width] == Tile.col_spawn_water) return Tile.t.spawn_water;
		return Tile.t.voidTile;
	}

}
