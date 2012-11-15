package player;

public class Board{
	
	private static final int NO = 1;
	private static final int NE = 2;
	private static final int EA = 3;
	private static final int SE = 4;
	private static final int SO = 5;
	private static final int SW = 6;
	private static final int WE = 7;
	private static final int NW = 8;
	
	private Chip[][] BoardSize; // Follow java convention where (0,0) is top left when it matters
	private int numWhite;	//number of white chips on board
	private int numBlack;	//number of black chips on board 
	
	
	public Board(){
		BoardSize = new Chip[8][8];
		numWhite = 0;
		numBlack = 0;
	}
	
	public Chip getContents(int x, int y){
		return BoardSize[x][y];	
	}
	
	public void addChip(int x, int y, int color){
		BoardSize[x][y] = new Chip(x, y, color);
		BoardSize[x][y].isTouched = neighbors(x, y, color);
		if(color == MachinePlayer.WHITE){
			numWhite++;
		}else{
			numBlack++;
		}
	}
	
	public int number(int color){
		if(MachinePlayer.BLACK == color){
			return numBlack;
		}
		return numWhite;
	}
		
	private boolean neighbors(int x, int y, int color){ // Returns true if it touches same color chip
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				
				if(x+i < 0 || x+i >7) continue;
				if(y+j < 0 || y+j >7) continue;
				if(BoardSize[x+i][y+j] == null) continue;
				else if(BoardSize[x+i][y+j].chipColor == color){
					BoardSize[x+i][y+j].isTouched = true;
					return true;
				}
			}
		}
		return false;	
	}
	
	private boolean neighbors(int x, int y, int x2, int y2, int color){ // Returns true if it touches same chip, ignores the second list coordinates (legality checking)
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				if((x+i)==x2 && (y+j)==y2) continue;
				
				if(x+i < 0 || x+i >7) continue;
				if(y+j < 0 || y+j >7) continue;
				if(BoardSize[x+i][y+j] == null) continue;
				if(BoardSize[x+i][y+j].chipColor == color){
					return true;
				}
			}
		}
		return false;	
	}	
	
	/** Checks if a move is legal on the current board
	*/
	public boolean isLegal(Move m, int color){
		if(m.moveKind == Move.QUIT) return true;
		if(m.moveKind == Move.STEP && m.x1 == m.x2 && m.y1 == m.y2) return false; 
		// Cant move to same place
		if(BoardSize[m.x1][m.y1] != null) return false; // Already occupied
		
		// Check the goal lines
		if(color == MachinePlayer.WHITE && m.y1 == 7 || m.y1 == 0)return false;
		if(color == MachinePlayer.BLACK && m.x1 == 7 || m.x1 == 0)return false;
		
		// Check the corners
		if(m.x1 == 0){
			if(m.y1 == 0 || m.y1 == 7) return false;
		}
		if(m.x1 == 7){
			if(m.y1 == 0 || m.y1 == 7) return false;
		}
		
		if(checkCluster(m, color)) return false;
		
		return true;
	}
	
	/** retun true if @m would form a cluster
	*/	
	private boolean checkCluster(Move m, int color){
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				
				if(m.x1+i < 0 || m.x1+i>7) continue;
				if(m.y1+j <0 || m.y1+j >7) continue;
				if(BoardSize[m.x1+i][m.y1+j] == null) continue;
				if(BoardSize[m.x1+i][m.y1+j].chipColor == color){
					if(neighbors(m.x1+i, m.y1+j, m.x1, m.y1, color)) return true;
				}
			}
		}
		return false;
	}
	/** Makes a spcified move on the internal game board
	*/
	
	/**
	 * 
	 * @param m
	 * @param oppColor
	 */
	public void makeMove(Move m, int color){
		switch(m.moveKind){
			
			case Move.QUIT: return;
			
			case Move.STEP:
				BoardSize[m.x1][m.y1] = new Chip(m.x1, m.y1, color); 
				BoardSize[m.x2][m.y2] = null;
			
			case Move.ADD:
				addChip(m.x1, m.y1, color);
		}
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	public boolean hasNetwork(int color){
		if(goalIsEmpty(color))
			return false;
		if(color == MachinePlayer.WHITE){
			for(int i = 1; i<7; i++){
				if(this.BoardSize[0][i] != null){
//				BoardSize = false; // for every chip
				return explore(this.BoardSize[0][i], 0, 0);
				}
				
				
			}
			return false;
		}
		else if(color == MachinePlayer.BLACK){
			for(int i = 1; i<7; i++){
				if(this.BoardSize[i][0] != null){
//				BoardSize = false; // for every chip
				return explore(this.BoardSize[i][0], 0, 0);
				}
				
			}
			return false;
			
		}
		else
			return false;
	}
	
	public boolean explore(Chip c, int length, int direction){ // diretction used to enforce turning requirement, length used to enforce rule
		Chip neighborChip; // Could need to Declare
		int colorChips;
		if(c.chipColor == MachinePlayer.BLACK){
			colorChips = numBlack;
		}else{
			colorChips = numWhite;
		}
		if(colorChips <6){
			return false;
		}
		
		c.isVisited = true;// makes sure we do not loop this location
		for(int i=0; i<colorChips; i++){
			neighborChip = getNeighbor(c, i);
			if(neighborChip.isVisited == true)
				continue;
			if(neighborChip != null){
				if (isStartGoal(neighborChip))
					continue;
				if(i == direction)
					continue;
				if(isEndGoal(neighborChip)){
					if(length>=5)
						return true;
				}
				else{
					if(explore(neighborChip, length++, i)){
						return true;
					}
				}
			}
				

		}
		c.isVisited = false;
		return false;
	}
	
	private NeighborChip getNeighbor(Chip c, int skipDirection){
		NeighborChip temp;
					
			for(int j=1; j<9; j++){
				if(j == skipDirection)continue;
			
				switch(j){
			
					case NO: 
						for(int i=1; i<=(8-c.x); i++){
							if(BoardSize[c.x][c.y-i]!= null){
								temp = (NeighborChip) BoardSize[c.x][c.y-i];
								temp.prevDir = i;
								return temp;
							}
						}
						
					case SO: 
						for(int i=1; i<=(8-c.x); i++){
							if(BoardSize[c.x][c.y+i].chipColor != c.chipColor){
								temp = (NeighborChip) BoardSize[c.x][c.y+i];
								temp.prevDir = i;
								return temp;
							}	
						}
					
						case WE: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x-i][c.y].chipColor == c.chipColor){
									temp = (NeighborChip) BoardSize[c.x-i][c.y];
									temp.prevDir = i;
									return temp;
								}
							}
					
						case EA: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x+i][c.y].chipColor == c.chipColor){
					 			temp = (NeighborChip) BoardSize[c.x+i][c.y];
								temp.prevDir = i;
								return temp;
							}
						}
					
						case NE: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x+i][c.y-i].chipColor == c.chipColor){
									temp = (NeighborChip) BoardSize[c.x+i][c.y-i];
									temp.prevDir = i;
									return temp;
								}
							}
					
						case SE: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x+i][c.y+i].chipColor == c.chipColor){
									temp = (NeighborChip) BoardSize[c.x+i][c.y+i];
									temp.prevDir = i;
									return temp;
								}
							}
					
						case SW: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x-i][c.y+i].chipColor == c.chipColor){
									temp = (NeighborChip) BoardSize[c.x-i][c.y+i];
									temp.prevDir = i;
									return temp;
								}
							}
					
						case NW: 
							for(int i=1; i<=(8-c.x); i++){
								if(BoardSize[c.x-i][c.y-i].chipColor == c.chipColor){
									temp = (NeighborChip) BoardSize[c.x-i][c.y-i];
									temp.prevDir = i;
									return temp;
								}
							}
						
						default:
							return null;
					}
				}
				return null;
	}
	
	/**
	* Private method to check if the goal of @color is empty
	*/
	private boolean goalIsEmpty(int color){

		if(color == MachinePlayer.WHITE){
			for(int i=1; i<7; i++){
				System.out.println("I is: "+i);
				if(BoardSize[0][i] == null|| BoardSize[7][i] == null) continue; 
				if(BoardSize[0][i].chipColor == color || BoardSize[7][i].chipColor == color) return false;
			}
			return true;
		}
		if(color == MachinePlayer.BLACK){
			for(int i=1; i<7; i++){
				if(BoardSize[i][0].chipColor == color || BoardSize[i][7].chipColor == color) return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	* Helper funciton used by explore to determine if a chip is in its 
	* own start goal
	*/
	private boolean isStartGoal(Chip c){
	
		if(c.chipColor == MachinePlayer.WHITE && c.x == 0) return true;
		if(c.chipColor == MachinePlayer.BLACK && c.y == 0) return true;
		return false;
	}	
	
	private boolean isEndGoal(Chip c){
		
		if(c.chipColor == MachinePlayer.WHITE && c.x == 7) return true;
		if(c.chipColor == MachinePlayer.BLACK && c.y == 7) return true;
		return false;
	}
	public boolean touched(int x, int y){
		return BoardSize[x][y].isTouched;
	}
				
	
	
}
