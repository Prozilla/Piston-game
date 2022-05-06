package source.main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import source.interactible.Piston;
import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	final int originalTileSize = 8;
	public final int scale = 6;
	public final int tileSize = originalTileSize * scale;

	public final int verticalTiles = 10;
	public final int horizontalTiles = 16;

	// Window settings
	final int width = horizontalTiles * tileSize;
	final int height = verticalTiles * tileSize;

	public int fps = 60;
	public double deltaTime = 0;

	TileManager tileManager = new TileManager(this);
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	// Player player = new Player(this, keyHandler);
	MouseListener mouseHandler = new MouseListener(this, tileManager);

	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;

	public GamePanel() {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.addMouseMotionListener(mouseHandler);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				Piston piston = mouseHandler.coordinateToPiston.get(mouseHandler.mouseCoordinate);
				if (piston != null) {
					piston.activate();
				}
			}
		});
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/fps;
		double delta = 0;

		double lastTime = System.nanoTime();
		long currentTime;

		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				deltaTime = delta;
				// System.out.println(deltaTime);

				update();
				repaint();

				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				// System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		// player.update();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D graphics2D = (Graphics2D)graphics;

		tileManager.draw(graphics2D);
		// player.draw(graphics2D);
		// mouseHandler.draw(graphics2D);

		graphics2D.dispose();
	}

}
