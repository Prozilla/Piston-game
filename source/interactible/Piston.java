package source.interactible;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Point;

import source.level.LevelManager;
import source.main.GamePanel;
import source.tile.TileManager;

public class Piston extends Interactible {

	final int borderWidth = 3;
	final float animationDuration = 0.2f;

	Point headOffsetPosition = new Point(0, 0);
	float headOffset = 0;
	boolean sticky = false;

	GamePanel gamePanel;
	TileManager tileManager;

	public Piston(GamePanel gamePanel, TileManager tileManager, int x, int y, String direction, BufferedImage sprite, boolean sticky) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.sprite = sprite;
		this.sticky = sticky;
	}

	public BufferedImage rotateImage(BufferedImage image, double degrees) {
		double rotationRequired = Math.toRadians (degrees);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public void activate() {
		active = true;

		state = "extending";
		headOffset = 0;

		updateHeadPosition();
	}

	public void updateHeadPosition() {
		int offset = (int)Math.ceil(headOffset * gamePanel.tileSize);
		headOffsetPosition = moveInDirection(offset);
	}

	public Point moveInDirection(int amount) {
		Point point = new Point(0, 0);

		switch (direction) {
			case "up":
				point = new Point(0, -amount);
				break;
			case "down":
				point = new Point(0, amount);
				break;
			case "right":
				point = new Point(amount, 0);
				break;
			case "left":
				point = new Point(-amount, 0);
				break;
		}

		return point;
	}

	public void moveHead() {
		Movable movable = getMovable(state == "retracting" && sticky ? 2 : 1);

		if (headOffset < 1 && state == "extending") {
			headOffset += gamePanel.deltaTime / (animationDuration / 2 * gamePanel.fps);

			if (headOffset > 1)
				headOffset = 1;

			updateHeadPosition();

			if (movable != null) {
				movable.offset = headOffsetPosition;
			}
		} else {
			if (state != "retracting") {
				// Only runs once, when pistons starts retracting
				state = "retracting";

				if (movable != null) {
					moveMovable(movable, headOffsetPosition);
				}
			}

			headOffset -= gamePanel.deltaTime / (animationDuration / 2 * gamePanel.fps);

			if (headOffset <= 0) {
				// Only runs once, when pistons finished retracting
				active = false;

				if (sticky && movable != null) {
					Point point = moveInDirection(-gamePanel.tileSize);
					moveMovable(movable, point);
				}
			} else if (sticky && movable != null) {
				movable.offset = moveInDirection(-(int)Math.ceil((1 - headOffset) * gamePanel.tileSize));
			}

			updateHeadPosition();
		}
	}

	public Movable getMovable(int distance) {
		Point offset = moveInDirection(gamePanel.tileSize * distance);
		Point coordinate = new Point((x + offset.x) / gamePanel.tileSize, (y + offset.y) / gamePanel.tileSize);
		Movable movable = tileManager.coordinateToMovable.get(coordinate);

		return movable;
	}

	public void moveMovable(Movable movable, Point offset) {
		movable.x += offset.x;
		movable.y += offset.y;
		movable.offset = new Point(0, 0);

		tileManager.coordinateToMovable.remove(movable.coordinate);
		movable.coordinate = new Point(movable.x / gamePanel.tileSize, movable.y / gamePanel.tileSize);
		tileManager.coordinateToMovable.put(movable.coordinate, movable);

		if (tileManager.targetCoordinates.contains(movable.coordinate)) {
			LevelManager.nextLevel();
		}
	}

	public void draw(Graphics2D graphics2D) {
		double rotation = 0;

		if (active)
			moveHead();

		switch (direction) {
			case "down":
				rotation = 180;
				break;
			case "left":
				rotation = 270;
				break;
			case "right":
				rotation = 90;
				break;
		}

		if (!active) {
			// Draw border when hovering
			// if (MouseListener.hoveringTile(x, y)) {
			// 	graphics2D.setColor(Color.white);
			// 	graphics2D.fillRect(x - borderWidth, y - borderWidth, gamePanel.tileSize + borderWidth * 2, gamePanel.tileSize + borderWidth * 2);
			// }

			graphics2D.drawImage(rotateImage(sprite, rotation), x, y, gamePanel.tileSize, gamePanel.tileSize, null);
		} else {
			if (headOffset > 0.8) {
				Point headExtensionOffset = moveInDirection(-gamePanel.tileSize);
				graphics2D.drawImage(rotateImage(splitSprite[2], rotation), x + headOffsetPosition.x + headExtensionOffset.x, y + headOffsetPosition.y + headExtensionOffset.y, gamePanel.tileSize, gamePanel.tileSize, null);
			}

			graphics2D.drawImage(rotateImage(splitSprite[1], rotation), x + headOffsetPosition.x, y + headOffsetPosition.y, gamePanel.tileSize, gamePanel.tileSize, null);
			graphics2D.drawImage(rotateImage(splitSprite[0], rotation), x, y, gamePanel.tileSize, gamePanel.tileSize, null);
		}
	}

}
