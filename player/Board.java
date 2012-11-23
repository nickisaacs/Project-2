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
		
	public Chip[][] BoardSize; // Follow java convention where (0,0) is top left when it matters
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
	
	public int totalChipCount(){
		return (numBlack + numWhite);
	}
	
	public void addChip(int x, int y, int color){
		
		BoardSize[x][y] = new Chip(x, y, color);
		BoardSize[x][y].isTouched = neighbors(x, y, color);
		if(color == MachinePlayer.WHITE){
			numWhite++;
			System.out.println("adding at: "+x+","+y+")");
		}else{
			numBlack++;
		}
		printBoard();
	}
	public void removeChip(int x, int y){
		if(BoardSize[x][y] != null){
			if(BoardSize[x][y].chipColor == MachinePlayer.WHITE){
				numWhite--;
			}else{
				numBlack--;
			}
			BoardSize[x][y] = null;
		}
	}
	
	public int number(int color){
		if(MachinePlayer.BLACK == color){
			return numBlack;
		}
		return numWhite;
	}
		
	private boolean neighbors(int x, int y, int color){ // Returns true if it touches same color chip
		
		if(numNeighbors(x,y,color) > 1) //easy clusters
			return true;
		
		
		
		boolean tempChipFlag = false;
		if(BoardSize[x][y] == null){
			Chip placeHolder = new Chip(x, y, color);
			BoardSize[x][y] = placeHolder;
			placeHolder.isVisited = true;
			tempChipFlag = true;
		}else{
			BoardSize[x][y].isVisited = true;
		}
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				
				if(x+i < 0 || x+i >7) continue;
				if(y+j < 0 || y+j >7) continue;
				if(BoardSize[x+i][y+j] == null) continue;
				if(BoardSize[x+i][y+j].isVisited == true) continue;
				if(BoardSize[x+i][y+j].chipColor == color){
					if(tempChipFlag){
						BoardSize[x][y] = null;
					}else{
						BoardSize[x][y].isVisited = false;
					}	
					return true;
				}else if(neighbors(x+i, y+j, x, y, color)){
					if(tempChipFlag){
						BoardSize[x][y] = null;
					}else{
						BoardSize[x][y].isVisited = false;
					}	
					return true;
				}
			}
		}
		
		
		if(tempChipFlag){
			BoardSize[x][y] = null;
		}else{
			BoardSize[x][y].isVisited = false;
		}	
		return false;	
	}	
	
	private boolean neighbors(int x, int y, int x2, int y2, int color){ // Returns true if it touches same chip, ignores the second list coordinates (legality checking)
		boolean tempChipFlag = false;
		if(BoardSize[x][y] == null){
			Chip placeHolder = new Chip(x, y, color);
			BoardSize[x][y] = placeHolder;
			placeHolder.isVisited = true;
			tempChipFlag = true;
		}else{
			BoardSize[x][y].isVisited = true;
		}
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				if((x+i)==x2 && (y+j)==y2) continue;
				
				if(x+i < 0 || x+i >7) continue;
				if(y+j < 0 || y+j >7) continue;
				if(BoardSize[x+i][y+j] == null) continue;
				if(BoardSize[x+i][y+j].isVisited == true) continue;
				if(BoardSize[x+i][y+j].chipColor == color){
					if(tempChipFlag){
						BoardSize[x][y] = null;
					}else{
						BoardSize[x][y].isVisited = false;
					}
					return true;
				}else if(neighbors(x+i, y+j, x, y, color)){
					if(tempChipFlag){
						BoardSize[x][y] = null;
					}else{
						BoardSize[x][y].isVisited = false;
					}
					return true;
				}
			}
		}
		if(tempChipFlag){
			BoardSize[x][y] = null;
		}else{
			BoardSize[x][y].isVisited = false;
		}	
		return false;	
	}
	
	public int numNeighbors(int x,int y, int color){
		int count = 0;
			for(int i=-1; i<2; i++){
				for(int j=-1; j<2; j++){
					if(x+i < 0 || x+i >7) continue;
					if(y+j < 0 || y+j >7) continue;
					if(BoardSize[x+i][y+j] == null)
						continue;
					if(BoardSize[x+i][y+j] != null){
						if(BoardSize[x+i][y+j].chipColor == color){
							count++;
							int x2 = x+i;
							int y2 = y+j;
							for(int a=-1; a<2; a++){
								for(int b=-1; b<2; b++){
										if(x2+a < 0 || x2+a >7) continue;
										if(y2+b < 0 || y2+b >7) continue;
										if(BoardSize[x2+a][y2+b] == null || (x2+a == x && y2+b ==y) || (a == 0 && b == 0)){
											continue;
										}else{
											if(BoardSize[x2+a][y2+b] != null){
												if(BoardSize[x2+a][y2+b].chipColor == color)
													count++;
											}
										}
								}
							}
						}
							
					}
				}
			}
		return count;
	}	
		
	
	/** Checks if a move is legal on the current board
	*/
	public boolean isLegal(Move m, int color){
		if(m.moveKind == Move.QUIT) return true;
		
		if(m.moveKind == Move.STEP && (m.x1 == m.x2 && m.y1 == m.y2)){
			//System.out.println("False at step move same place");
			return false;
		}
		// Cant move to same place/existing chip
		if(BoardSize[m.x1][m.y1] != null){
			//System.out.println("False at occupied");
			return false; // Already occupied
		}
		
		// Check the goal lines
		if(color == MachinePlayer.WHITE){
			if(m.y1 == 7 || m.y1 == 0){
				//System.out.println("False at goal line 1");
				return false;
			}
		}
		if(color == MachinePlayer.BLACK){
			if(m.x1 == 7 || m.x1 == 0){
				//System.out.println("False at goal line 2");
				return false;
			}
		}
		// Check the corners
		if(m.x1 == 0){
			if(m.y1 == 0 || m.y1 == 7){
				//System.out.println("False at corner");
				return false;
			}
		}
		if(m.x1 == 7){
			if(m.y1 == 0 || m.y1 == 7){
				//System.out.println("False at corner");
				return false;
			}
		}
		
		/*if(m.moveKind == Move.STEP && neighbors(m.x1, m.y1, m.x2, m.y2, color)){
			//System.out.println("False at cluster");
			return false;
		}*/
		//New Cluster Checker!!
		if(numNeighbors(m.x1,m.y1,color) > 1) //easy clusters
			return false;
		/*if(m.moveKind == Move.ADD && neighbors(m.x1, m.y1, color)){
			//System.out.println("False at cluster");
			return false;
		}*/
		
		return true;
	}
	
	/** retun true if @m would form a cluster
	*/	
	private boolean checkCluster(Move m, int color){
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue; // Don't check current location
				
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
	
	public void unmakeMove(Move m){
		switch(m.moveKind){
			
			case Move.STEP:
				BoardSize[m.x2][m.y2] = BoardSize[m.x1][m.y1]; 
				BoardSize[m.x1][m.y1] = null; 
			
			case Move.ADD:
				removeChip(m.x1, m.y1);
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
		
		boolean recurse = true;
		Chip closeNeighbor = null;
		int colorChips;
		if(c.chipColor == MachinePlayer.BLACK){
			colorChips = numBlack;
		}
		else{
			colorChips = numWhite;
		}
		if(colorChips <6){
			return false;
		}
		c.isVisited = true;// makes sure we do not loop this location
		for(int z=1; z<9; z++){
			closeNeighbor = getNeighbor(c, z);
			
			if(closeNeighbor == null) continue;
			
			if(closeNeighbor.chipColor == c.chipColor && z == direction) 
				continue;
			if(closeNeighbor.chipColor != c.chipColor) continue;
			
			if(closeNeighbor.isVisited == true) continue;
				
			if(isStartGoal(closeNeighbor))continue;
				
			if(isEndGoal(closeNeighbor) && length >= 5){
				return true;
			}else{
					
				if(explore(closeNeighbor, length++, z)){
					c.isVisited = false; //change
					return true; // pop stack
						
				}
			}
		}
		
		c.isVisited = false;
		return false;
	}
		
	private Chip getNeighbor(Chip c, int skipDirection){
		Chip temp;
					
				switch(skipDirection){
			
					case NO: 
						for(int i=1; i<=(c.y); i++){
							if(BoardSize[c.x][c.y-i]!= null){
								temp = BoardSize[c.x][c.y-i];
								return temp;
							}
						}
						
					case SO: 
						for(int i=1; i<=(7-c.y); i++){
							if(BoardSize[c.x][c.y+i]!= null){
								temp = BoardSize[c.x][c.y+i];
								return temp;
							}	
						}
					
					case WE: 
						for(int i=1; i<=(c.x); i++){
							if(BoardSize[c.x-i][c.y]!= null){
								temp = BoardSize[c.x-i][c.y];
								return temp;
							}
						}
					
					case EA: 
						for(int i=1; i<=(7-c.x); i++){
							if(BoardSize[c.x+i][c.y]!= null){
							temp = BoardSize[c.x+i][c.y];
							return temp;
						}
					}
					
					case NE: 
						for(int i=1; i<=(7-c.x); i++){
							for(int z=1; z<=(c.y); z++){
								
								if((c.x+i)>7 || (c.y-z)<1) break;
								if(BoardSize[c.x+i][c.y-z] != null){
									temp = BoardSize[c.x+i][c.y-z];
									return temp;
								}
							}
						}
					
					case SE: 
						for(int i=1; i<=(7-c.x); i++){
							for(int z=1; z<=(7-c.y); z++){
								
								if((c.y+z) > 7 || (c.x+i) > 7) break;
								if(BoardSize[c.x+i][c.y+z] != null){
									temp = BoardSize[c.x+i][c.y+z];
									return temp;
								}
							}
						}
					
					case SW: 
						for(int i=1; i<=(7-c.x); i++){
							for(int z=1; z<=(7-c.y); z++){
								
								if((c.y+z) > 7 || (c.x-i) < 1) break;
								if(BoardSize[c.x-i][c.y+z] != null){
									temp = BoardSize[c.x-i][c.y+z];
									return temp;
								}
							}
						}
					
					case NW: 
						for(int i=1; i<=(7-c.x); i++){
							for(int z=1; z<=(c.y); z++){
								
								if((c.y+z) < 7 || (c.x-i) < 1) break;
								if(BoardSize[c.x-i][c.y-z] != null){
									temp = BoardSize[c.x-i][c.y-z];
									return temp;
								}
							}
						}
					
					default:
						return null;
					}
				//return null;
	}
	
	/**
	* Private method to check if the goal of @color is empty
	*/
	private boolean goalIsEmpty(int color){

		if(color == MachinePlayer.WHITE){
			for(int i=1; i<7; i++){
				if(BoardSize[0][i] == null|| BoardSize[7][i] == null) continue; 
				if(BoardSize[0][i].chipColor == color || BoardSize[7][i].chipColor == color) return false;
			}
			return true;
		}
		if(color == MachinePlayer.BLACK){
			for(int i=1; i<7; i++){
				if(BoardSize[i][0] == null|| BoardSize[i][7] == null) continue; 
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
	
	public void printBoard(){
		for(int i=0; i<8; i++){
			System.out.print("|");
			for(int j=0; j<8; j++){
				try{
					if(BoardSize[j][i] == null){
						System.out.print(" |");
					}else{
						if(BoardSize[j][i].chipColor == MachinePlayer.BLACK){
							System.out.print("B|");
						}else{
							System.out.print("W|");
						}
					}
				}
				catch(NullPointerException e){
					System.out.println("failed to print board contents at: ("+j+","+i+")");
				} 
			}
			System.out.print("\n");
			
		}
		System.out.print("\n");
		System.out.print("-----------------------------");
		System.out.println("\n");
	}
				
		
	
	
}
