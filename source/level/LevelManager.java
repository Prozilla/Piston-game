package source.level;

import source.tile.TileManager;

public class LevelManager {

	public static Level currentLevel;
	public static int currentLevelIndex = 0;

	static TileManager tileManager; 
	
	public static Level[] levels = new Level[]{
		new Level("tutorial1", "stone", "stone_bricks", "red_wool", "target"),
		new Level("level1", "cyan_concrete", "red_concrete", "yellow_concrete", "target"),
		new Level("level1", "deepslate", "deepslate_top", "oak_log_top", "target"),
	};

	public LevelManager(TileManager tileManager) {
		LevelManager.tileManager = tileManager;
		LevelManager.setLevel(0);
	}

	public static void setLevel(int index) {
		currentLevelIndex = index;
		currentLevel = levels[currentLevelIndex];

		tileManager.loadLevel();
	}

	public static void nextLevel() {
		currentLevelIndex++;

		if (currentLevelIndex == levels.length)
			currentLevelIndex = 0;

		setLevel(currentLevelIndex);
	}

}
