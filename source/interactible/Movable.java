package source.interactible;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import source.main.GamePanel;

public class Movable extends Interactible {
	Point offset = new Point(0, 0);
	public Point coordinate = new Point(0, 0);

	GamePanel gamePanel;

	public Movable(GamePanel gamePanel, int x, int y, BufferedImage sprite) {
		this.gamePanel = gamePanel;
		this.x = x;
		this.y = y;
		this.coordinate = new Point(x / gamePanel.tileSize, y / gamePanel.tileSize);
		this.sprite = sprite;
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.drawImage(sprite, x + offset.x, y + offset.y, gamePanel.tileSize, gamePanel.tileSize, null);
	}
}
