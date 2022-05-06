package source.main;

import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Cursor;

import source.interactible.Piston;
import source.tile.TileManager;

public class MouseListener implements MouseMotionListener {
	GamePanel gamePanel;
	TileManager tileManager;

	static int tileSize;

	public static Point mousePosition = new Point(0, 0);
	public static Point mouseCoordinate = new Point(0, 0);

	public static Map<Point, Piston> coordinateToPiston = new HashMap<Point, Piston>();

	public MouseListener(GamePanel gamePanel, TileManager tileManager) {
		this.gamePanel = gamePanel;
		this.tileManager = tileManager;
		tileSize = gamePanel.tileSize;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		// System.out.println("Drag");
		// gamePanel.handleClick();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		Point point = event.getPoint();
		Point coordinate = new Point(point.x / gamePanel.tileSize, point.y / gamePanel.tileSize);

		if (mouseCoordinate.equals(coordinate))
			return;

		mousePosition = point;
		mouseCoordinate = coordinate;

		// System.out.println(mousePosition);

		updateCursor();
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.setColor(Color.white);
		graphics2D.drawOval(mousePosition.x, mousePosition.y, 8, 8);
	}

	// Store piston postions in map (convert mouse position to tile coordinate)
	public static boolean hoveringTile(int x, int y) {
		Point point = new Point(x / tileSize, y / tileSize);
		return mouseCoordinate.equals(point);
	}

	public void updateCursor() {
		boolean hoveringPiston = coordinateToPiston.containsKey(mouseCoordinate);

		if (hoveringPiston) {
			Main.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			Main.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
