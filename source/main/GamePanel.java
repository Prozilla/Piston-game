package source.main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import source.interactible.Piston;
import source.level.LevelManager;
import source.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen settings
	final static int originalTileSize = 8;
	public final static int pixelScale = 6;
	public final static int tileSize = originalTileSize * pixelScale;

	public final int horizontalTiles = 16;
	public final int verticalTiles = 10;

	// Window settings
	final int width = horizontalTiles * tileSize;
	final int height = verticalTiles * tileSize;

	public int fps = 60;
	public double deltaTime = 0;

	TileManager tileManager = new TileManager(this);
	LevelManager levelManager = new LevelManager(tileManager);
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	MouseListener mouseHandler = new MouseListener(this, tileManager);

	public GamePanel() {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.addMouseMotionListener(mouseHandler);
		this.setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handleClick();
			}
		});
	}

	public void handleClick() {
		Piston piston = MouseListener.coordinateToPiston.get(MouseListener.mouseCoordinate);
		if (piston != null) {
			piston.activate();
		}
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

				update();
				repaint();

				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
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

		graphics2D.dispose();
	}

}
