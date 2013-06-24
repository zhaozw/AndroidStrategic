package jonathan.geoffroy.androidstrategic.model.mapping;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.imageio.ImageIO;

import jonathan.geoffroy.androidstrategic.model.fighters.Fighter;
import jonathan.geoffroy.androidstrategic.model.items.Item;
import jonathan.geoffroy.androidstrategic.model.mapping.magic.Magic;
import jonathan.geoffroy.androidstrategic.model.utils.Coord2D;

public class Map {
	public final static int CLOSEDDOOR = -16777216, DESERT = -459750, FLOOR = -14750720, FOREST = -13525728, 
			FORT = -9539712, GATE = -15654456, GRASS = -10377914, MOUNTAIN = -7713017, 
			PEAK = -3184105, PILLAR = -5395027, PIT = -10596800, PLAIN = -4325736, 
			ROAD = -33024, SEA = -11602448, VILLAGE = -655362;
	public final static String SCENARII_DIR = "data/scenarii/";

	private Terrain[][] map;
	private LinkedHashSet<Terrain> terrains;
	private HashMap<Coord2D, Magic> terrainSpecs;
	private HashMap<Coord2D, Item> terrainsItems;
	private HashMap<Fighter, Coord2D> fighters;

	public Map() {
		terrains = new LinkedHashSet<Terrain>();
		terrainSpecs = new HashMap<Coord2D, Magic>();
		terrainsItems = new HashMap<Coord2D, Item>();
		fighters = new HashMap<Fighter, Coord2D>();
	}

	public static Map load(String scenarioName, int chapterNum)
			throws IOException {
		Map loadedMap = new Map();

		loadedMap.loadTerrains(scenarioName, chapterNum);
		return loadedMap;
	}

	private void loadTerrains(String scenarioName, int chapterNum)
			throws IOException {
		HashMap<Integer, Terrain> createdTerrains = new HashMap<Integer, Terrain>();
		createdTerrains.put(CLOSEDDOOR, new ClosedDoor());
		createdTerrains.put(DESERT, new Desert());
		createdTerrains.put(FLOOR, new Floor());
		createdTerrains.put(FOREST, new Forest());
		createdTerrains.put(FORT, new Fort());
		createdTerrains.put(GATE, new Gate());
		createdTerrains.put(GRASS, new Grass());
		createdTerrains.put(MOUNTAIN, new Mountain());
		createdTerrains.put(PEAK, new Peak());
		createdTerrains.put(PILLAR, new Pillar());
		createdTerrains.put(PIT, new Pit());
		createdTerrains.put(PLAIN, new Plain());
		createdTerrains.put(ROAD, new Road());
		createdTerrains.put(SEA, new Sea());
		createdTerrains.put(VILLAGE, new Village());

		BufferedImage img;
		try {
			img = ImageIO.read(new FileInputStream(SCENARII_DIR + scenarioName + "/" + chapterNum + "_map.png"));
			int width = img.getWidth();
			int height = img.getHeight();
			map = new Terrain[height][width];
			int pixel;
			Terrain t;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					pixel = img.getRGB(j, i);
					t = createdTerrains.get(pixel);
					if (t == null) {
						throw new IOException("this terrain is NOT available");
					}
					map[i][j] = t;
					terrains.add(t);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * test if attacker can hit defender, depending on they own Terrain, and the attacker's range
	 * @param attacker Fighter who attack defender
	 * @param defender Fighter assaulted
	 * @return TRUE if attacker can hit defender, depending on they own Terrain, and the attacker's range 
	 */
	public boolean canHit(Fighter attacker, Fighter defender) {
		assert(fighters.containsKey(attacker));
		assert(fighters.containsKey(defender));
		
		Coord2D coordAttacker = fighters.get(attacker);
		Coord2D coordDefender = fighters.get(defender);
		int attackerMinRange = attacker.minRange();
		int attackerMaxRange = attacker.maxRange();
		
		int manhattanDistance = Math.abs(coordAttacker.x - coordDefender.x) + Math.abs(coordAttacker.y - coordDefender.y);
		return attackerMinRange >= manhattanDistance && attackerMaxRange <= manhattanDistance;
	}
	
	public Terrain getTerrain(int x, int y) {
		return map[y][x];
	}

	public Terrain[][] getMap() {
		return map;
	}

	public void setMap(Terrain[][] map) {
		this.map = map;
	}

	/**
	 * Add a non previously added fighter on the Map, to an empty Coord2D(x, y)
	 * @param fighter
	 * @param x
	 * @param y
	 */
	public void addFighter(Fighter fighter, int x, int y) {
		assert(!fighters.containsKey(fighter));
		assert(y >= 0 && y < map.length);
		assert(x >= 0 && x < map[y].length);
		fighters.put(fighter, new Coord2D(x, y));
	}

	/**
	 * Move a previously added fighter to an empty Coord2D(x, y);
	 * @param fighter
	 * @param x
	 * @param y
	 */
	public void moveFighter(Fighter fighter, int x, int y) {
		assert(fighters.containsKey(fighter));
		assert(y >= 0 && y < map.length);
		assert(x >= 0 && x < map[y].length);
		assert(!fighters.containsValue(new Coord2D(x, y)));
		Coord2D coord = fighters.get(fighter);
		coord.x = x;
		coord.y = y;
	}

	/** 
	 * remove a previously added fighter
	 * @param fighter
	 */
	public void rmFighter(Fighter fighter) {
		assert(fighters.containsKey(fighter));
		fighters.remove(fighter);
	}
}