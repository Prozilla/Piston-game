package source.entity;

import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import source.main.GamePanel;
import source.main.KeyHandler;

public class Player extends Entity {

	GamePanel gamePanel;
	KeyHandler keyHandler;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {
		this.gamePanel = gamePanel;
		this.keyHandler = keyHandler;

		setDefaultValues();
		getPlayerImages();
	}

	public void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		direction = "down";
	}

	public void getPlayerImages() {
		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("../../resources/player/iron_block.png"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void update() {
		if (keyHandler.upPressed != keyHandler.downPressed) {
			if (keyHandler.upPressed) {
				direction = "up";
				y -= speed;
			} else if (keyHandler.downPressed) {
				direction = "down";
				y += speed;
			}
		}

		if (keyHandler.leftPressed != keyHandler.rightPressed) {
			if (keyHandler.leftPressed) {
				direction = "left";
				x -= speed;
			} else if (keyHandler.rightPressed) {
				direction = "right";
				x += speed;
			}
		}
	}

	public void draw(Graphics2D graphics2D) {
		BufferedImage image = sprite;
		graphics2D.drawImage(image, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
	}

}
