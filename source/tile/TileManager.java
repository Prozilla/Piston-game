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

public class TileManager {

	final String backgroundTile = "stone";
	final String foregroundTile = "stone_bricks";
	final String movableTile = "red_wool";

	final String tilesPath = "../../textures/tiles/";

	GamePanel gamePanel;
	Map<String, Tile> tiles;
	int levelTiles[][];
	int levelTilesDirections[][];
	boolean generatedTiles = false;

	public ArrayList<Piston> pistons = new ArrayList<Piston>();
	public Map<Point, Movable> coordinateToMovable = new HashMap<Point, Movable>();

	public TileManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

		tiles = new HashMap<String, Tile>();
		levelTiles = new int[gamePanel.horizontalTiles][gamePanel.verticalTiles];
		levelTilesDirections = new int[gamePanel.horizontalTiles][gamePanel.verticalTiles];

		addTiles();
		loadLevel();
	}

	public void addTiles() {
		addTile(backgroundTile, false, false, true);
		addTile(foregroundTile, true, false, false);
		addTile(movableTile, true, true, false);
		addTile("piston", true, false, false);
		addTile("piston_body", true, false, false);
		addTile("piston_head", true, false, false);
		addTile("sticky_piston", true, false, false);
		addTile("sticky_piston_head", true, false, false);
	}

	public void addTile(String name, boolean collision, boolean movable, boolean background) {
		try {
			tiles.put(name, new Tile(name, ImageIO.read(getClass().getResourceAsStream(String.format("%s%s.png", tilesPath, name))), collision, movable, background));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void loadLevel() {
		try {
			InputStream inputStream = getClass().getResourceAsStream("../../levels/level1.txt");
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

		while(column < gamePanel.horizontalTiles && row < gamePanel.verticalTiles) {
			Point coordinate = new Point(x / gamePanel.tileSize, y / gamePanel.tileSize);
			int tileNumber = levelTiles[column][row];
			int tileDirection = levelTilesDirections[column][row];
			String tileName;

			switch (tileNumber) {
				case 0:
					tileName = backgroundTile;
					break;
				case 1:
					tileName = foregroundTile;
					break;
				case 2:
					tileName = "piston";
					break;
				case 3:
					tileName = "sticky_piston";
					break;
				case 4:
					tileName = movableTile;
					break;
				default:
					tileName = backgroundTile;
			}

			Tile tile = tiles.get(tileName);
			drawTile(graphics2D, tiles.get(backgroundTile).sprite, true, x, y);

			// if (tileName == backgroundTile)
			// 	continue;

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
						piston.splitSprite = new BufferedImage[]{tiles.get("piston_body").sprite, tiles.get("piston_head").sprite};
					} else {
						piston.splitSprite = new BufferedImage[]{tiles.get("piston_body").sprite, tiles.get("sticky_piston_head").sprite};
					}

					pistons.add(piston);
					MouseListener.coordinateToPiston.put(coordinate, piston);
				}
			} else if (tile.name == movableTile || coordinateToMovable.containsKey(coordinate)) {
				Movable movable = coordinateToMovable.get(coordinate);

				if (!generatedTiles && movable == null) {
					movable = new Movable(gamePanel, x, y, tile.sprite);
					coordinateToMovable.put(coordinate, movable);
				}
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
		generatedTiles = true;
	}

	// TO DO: implement random rotation
	public void drawTile(Graphics2D graphics2D, BufferedImage sprite, boolean background, int x, int y) {
		graphics2D.drawImage(sprite, x, y, gamePanel.tileSize, gamePanel.tileSize, null);

		if (background) {
			graphics2D.setColor(new Color(0.2f, 0.2f, 0.2f, 0.40f));
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
