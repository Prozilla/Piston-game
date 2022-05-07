package source.level;

public class Level {
	public String name;

	public String backgroundTile;
	public String foregroundTile;
	public String movableTile;
	public String targetTile;

	public Level(String name, String backgroundTile, String foregroundTile, String movableTile, String targetTile) {
		this.name = name;
		this.backgroundTile = backgroundTile;
		this.foregroundTile = foregroundTile;
		this.movableTile = movableTile;
		this.targetTile = targetTile;
	}
}
