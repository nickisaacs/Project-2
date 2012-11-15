package player;

public class Chip{
	
	public int x, y;
	public int chipColor;
	public boolean isTouched; // For clustering
	public boolean isVisited; // Is it touched in a line
	public int prevDir; // Used in explore for the direction of the previous call
	
	Chip(int x, int y, int color){
		this.x = x;
		this.y = y;
		this.chipColor = color;	
	}
}
	
	
