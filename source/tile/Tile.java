package source.tile;

import java.awt.image.BufferedImage;

public class Tile {

	public String name;
	public BufferedImage sprite;
	public boolean collision;
	public boolean movable;
	public boolean background;

	public Tile(String name, BufferedImage sprite, boolean collision, boolean movable, boolean background) {
		this.name = name;
		this.sprite = sprite;
		this.collision = collision;
		this.movable = movable;
		this.background = background;
	}
	
}
