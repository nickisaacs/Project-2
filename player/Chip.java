package player;

class Chip{
	
	public int x, y;
	public int chipColor;
	public boolean isTouched; // For clustering
	public boolean isited; // Is it touched in a line
	
	Chip(int x, int y, int color){
		this.x = x;
		this.y = y;
		this.chipColor = color;	
	}
}
	
	