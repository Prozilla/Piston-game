package source.interactible;

import java.awt.image.BufferedImage;

public class Interactible {

	public int x, y;
	public boolean active = false;
	public String state = "none";

	public BufferedImage sprite;
	public BufferedImage[] splitSprite;
	public String direction;
	
}