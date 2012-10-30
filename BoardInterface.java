interface Board{
	
	/** 
	* Constructs a blank board
	* the board will be indexed with (0,0) being in the top left
	*/
	public Board()
	
	/**
	* gets the chip at a given (x, y) location
	*/
	public Chip getContents(int x, int y)
	
	/**
	* Adds a chip at location (x, y) with color @color
	*/
	
	public void addChip(int x, int y, int color)
	
	/**
	* returns true if a move is legal
	*/
	
	public boolean isLegal(Move M, int color)
	
	/**
	* Makes the specified move on the internal game board
	*/
	
	public void makeMove(Move m, int oppColor)
	
	/**
	* Explores possible moves on a board to a specified depth and
	* returns the highest possible move 
	*/
	
	public int explore(Chip c int length, int direction)
}