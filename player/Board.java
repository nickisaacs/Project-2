package player

class Board{
	
	private Chip[][] BoardSize; // Follow java convention where (0,0) is top left when it matters
	private int numWhite;
	private int numBlack;
	
	
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
		BoardSize[x][y].isTouched = BoardSize.neighbors(x, y);
		if(color == WHITE){
			numWhite++;
		}else{
			numBlack++;
		}
	}
		
	private boolean neighbors(int x, int y, int color){ // Returns true if it touches same color chip
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				
				else if(BoardSize[x+1][y+1].chipColor == color){
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
				
				else if(BoardSize[x+i][y+j].chipColor == color){
					return true;
				}
			}
		}
		return false;	
	}	
	
	/** Checks if a move is legal on the current board
	*/
	public boolean isLegal(Move m, int color){
		if(m.moveKind == QUIT) return true;
		if(m.moveKind == STEP && x1 == x2 && y1 == y2) return false; // Cant move to same place
		if(BoardSize[m.x1][m.y1] != null) return false; // Already occupied
		
		// Check the goal lines
		if(color == WHITE && m.x1 == 7 || m.x1 == 0)return false;
		if(color == BLACK && m.y1 == 7 || m.y1 == 0)return false;
		
		// Check the corners
		if(m.x1==0 && m.y1==0 || 
		
		if(checkCluster(m, color)) return false;
	}
	
	/** retun true if @m would form a cluster
	*/	
	private boolean checkCluster(Move m, int color){
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i==0 && j==0) continue;
				if(BoardSize[m.x1+1][m.y1+j].color == color){
					if(neighbors(m.x1+1, m.y1+j, m.x1, m.y1, color)) return true;
				}
			}
		}
		return false;
	}
	
	
}