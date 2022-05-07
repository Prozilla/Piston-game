package source.tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import source.main.GamePanel;
import source.main.MouseListener;
import source.interactible.Movable;
import source.interactible.Piston;
import source.level.Level;
import source.level.LevelManager;

public class TileManager {

	final String tilesPath = "../../textures/tiles/";

	GamePanel gamePanel;

	Map<String, Tile> tiles;
	int levelTiles[][];
	int levelTilesDirections[][];
	boolean generatedTiles;

	public ArrayList<Piston> pistons;
	public Map<Point, Movable> coordinateToMovable;
	public ArrayList<Point> targetCoordinates;

	public TileManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void addTiles() {
		tiles = new HashMap<String, Tile>();

		addTile(LevelManager.currentLevel.backgroundTile, false, false, true);
		addTile(LevelManager.currentLevel.foregroundTile, true, false, false);
		addTile(LevelManager.currentLevel.movableTile, true, true, false);
		addTile(LevelManager.currentLevel.targetTile, false, false, true);
		addTile("piston", true, false, false);
		addTile("piston_body", true, false, false);
		addTile("piston_head", true, false, false);
		addTile("piston_head_extension", true, false, false);
		addTile("sticky_piston", true, false, false);
		addTile("sticky_piston_head", true, false, false);
	}

	public void addTile(String name, boolean collision, boolean movable, boolean background) {
		try {
			tiles.put(name, new Tile(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", tilesPath, name))), collision, movable, background));
		} catch (IOException exception) {
			// System.out.println("Invalid image path: " + String.format("%s%s.png", tilesPath, name));
			exception.printStackTrace();
		}
	}

	public void loadLevel() {
		addTiles();

		levelTiles = new int[gamePanel.horizontalTiles][gamePanel.verticalTiles];
		levelTilesDirections = new int[gamePanel.horizontalTiles][gamePanel.verticalTiles];

		pistons = new ArrayList<Piston>();
		coordinateToMovable = new HashMap<Point, Movable>();
		targetCoordinates = new ArrayList<Point>();

		MouseListener.coordinateToPiston = new HashMap<Point, Piston>();

		generatedTiles = false;

		try {
			InputStream inputStream = getClass().getResourceAsStream(String.format("../../levels/%s.txt", LevelManager.currentLevel.name));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			int column = 0;
			int row = 0;

			while(column < gamePanel.horizontalTiles && row < gamePanel.verticalTiles) {
				String line = bufferedReader.readLine();

				while (column < gamePanel.horizontalTiles) {
					String numbers[] = line.split(" ");

					int number = Integer.parseInt(String.valueOf(numbers[column].charAt(0)));
					int direction = Integer.parseInt(String.valueOf(numbers[column].charAt(1)));

					levelTiles[column][row] = number;
					levelTilesDirections[column][row] = direction;
					column++;
				}

				if (column == gamePanel.horizontalTiles) {
					column = 0;
					row++;
				}
			}

			bufferedReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void draw(Graphics2D graphics2D) {
		int column = 0;
		int row = 0;

		int x = 0;
		int y = 0;

		Level level = LevelManager.currentLevel;

		while(column < gamePanel.horizontalTiles && row < gamePanel.verticalTiles) {
			Point coordinate = new Point(x / gamePanel.tileSize, y / gamePanel.tileSize);
			int tileNumber = levelTiles[column][row];
			int tileDirection = levelTilesDirections[column][row];
			String tileName;

			switch (tileNumber) {
				case 0:
					tileName = LevelManager.currentLevel.backgroundTile;
					break;
				case 1:
					tileName = LevelManager.currentLevel.foregroundTile;
					break;
				case 2:
					tileName = "piston";
					break;
				case 3:
					tileName = "sticky_piston";
					break;
				case 4:
					tileName = LevelManager.currentLevel.movableTile;
					break;
				case 5:
					tileName = LevelManager.currentLevel.targetTile;
					break;
				default:
					tileName = LevelManager.currentLevel.backgroundTile;
			}

			Tile tile = tiles.get(tileName);
			drawTile(graphics2D, tiles.get(LevelManager.currentLevel.backgroundTile).sprite, true, x, y);

			if (tileName == "piston" || tileName == "sticky_piston") {
				String direction;

				switch (tileDirection) {
					case 1:
						direction = "right";
						break;
					case 2:
						direction = "down";
						break;
					case 3:
						direction = "left";
						break;
					default:
						direction = "up";
						break;
				}

				if (!generatedTiles) {
					Boolean sticky = tileName == "sticky_piston";

					Piston piston = new Piston(gamePanel, this, x, y, direction, tile.sprite, sticky);

					if (!sticky) {
						piston.splitSprite = new BufferedImage[]{tiles.get("piston_body").sprite, tiles.get("piston_head").sprite, tiles.get("piston_head_extension").sprite};
					} else {
						piston.splitSprite = new BufferedImage[]{tiles.get("piston_body").sprite, tiles.get("sticky_piston_head").sprite, tiles.get("piston_head_extension").sprite};
					}

					pistons.add(piston);
					MouseListener.coordinateToPiston.put(coordinate, piston);
				}
			} else if (tile.name == LevelManager.currentLevel.movableTile || coordinateToMovable.containsKey(coordinate)) {
				Movable movable = coordinateToMovable.get(coordinate);

				if (!generatedTiles && movable == null) {
					movable = new Movable(gamePanel, x, y, tile.sprite);
					coordinateToMovable.put(coordinate, movable);
				}
			} else if (tile.name == LevelManager.currentLevel.targetTile) {
				if (!targetCoordinates.contains(coordinate)) {
					targetCoordinates.add(coordinate);
				}

				drawTile(graphics2D, tile.sprite, tile.background, x, y);
			} else {
				drawTile(graphics2D, tile.sprite, tile.background, x, y);
			}

			column++;
			x += gamePanel.tileSize;

			if (column == gamePanel.horizontalTiles) {
				column = 0;
				x = 0;

				row++;
				y += gamePanel.tileSize;
			}
		}

		drawPistons(graphics2D);
		drawMovables(graphics2D);

		if (level == LevelManager.currentLevel)
			generatedTiles = true;
	}

	// TO DO: implement random rotation
	public void drawTile(Graphics2D graphics2D, BufferedImage sprite, boolean background, int x, int y) {
		graphics2D.drawImage(sprite, x, y, gamePanel.tileSize, gamePanel.tileSize, null);

		if (background) {
			graphics2D.setColor(new Color(0.2f, 0.2f, 0.2f, 0.50f));
			graphics2D.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);
		}
	}

	public void drawPistons(Graphics2D graphics2D) {
		for (int i = 0; i < pistons.size(); i++) {
			pistons.get(i).draw(graphics2D);
		}
	}

	public void drawMovables(Graphics2D graphics2D) {
		for (Movable movable : coordinateToMovable.values()) {
			movable.draw(graphics2D);
		}
	}

}
