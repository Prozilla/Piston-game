package source.level;

import source.tile.TileManager;

public class LevelManager {

	public static Level currentLevel;
	public static int currentLevelIndex = 0;

	static TileManager tileManager; 
	
	public static Level[] levels = new Level[]{
		new Level("tutorial1", "stone", "stone_bricks", "red_wool", "target"),
		new Level("tutorial2", "stone", "stone_bricks", "red_wool", "target"),
		new Level("tutorial3", "stone", "stone_bricks", "red_wool", "target"),
		new Level("tutorial4", "stone", "stone_bricks", "red_wool", "target"),
		new Level("level1", "stone", "stone_bricks", "red_wool", "target"),
		new Level("level2", "deepslate", "deepslate_top", "oak_log_top", "target"),
		new Level("level3", "cyan_concrete", "red_concrete", "yellow_concrete", "target"),
		new Level("level3", "nether_bricks", "netherrack", "obsidian", "target"),
	};

	public LevelManager(TileManager tileManager) {
		LevelManager.tileManager = tileManager;
		LevelManager.setLevel(6);
	}

	public static void setLevel(int index) {
		if (index >= levels.length) {
			currentLevelIndex = 0;
		} else {
			currentLevelIndex = index;
		}

		currentLevel = levels[currentLevelIndex];

		tileManager.loadLevel();
	}

	public static void nextLevel() {
		setLevel(currentLevelIndex + 1);
	}

}
