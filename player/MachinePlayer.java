/* MachinePlayer.java */

package player;
import java.util.Random;
import java.lang.Math;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  public final static int WHITE = 1;
  public final static int BLACK = 0;
  public int color;
  public int oppColor;
  public int searchDepth = 3;
  private int numChips;
  protected Board internal;
  private Move[] lastMovesMade;
  
  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
  	this(color, 1);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
	lastMovesMade = new Move[3];
  	internal = new Board();
  	this.color = color;
  	//this.searchDepth = searchDepth;
  	if(color==WHITE){
  		oppColor = BLACK;
  	}else{
  		oppColor = WHITE;
  	}
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  
	  if(numChips < 10){
		Move chosenMove = (Move) addMoveScore(this.color);
		internal.makeMove(chosenMove,color);
		numChips++;
		internal.printBoard();
		return chosenMove;
	  }else{
			Move chosenMove = stepMoveScore();
			if(staleMate(chosenMove)){
				System.out.println("Reached this");
				Move staleBreaker = staleMove();
				updateMoves(staleBreaker);
				internal.makeMove(staleBreaker,color);
				numChips++;
				internal.printBoard();
				return chosenMove;
			}
			updateMoves(chosenMove);
			internal.makeMove(chosenMove,color);
			numChips++;
			internal.printBoard();
			return chosenMove;
		}
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
  	if(internal.isLegal(m, oppColor)){
  		internal.makeMove(m, oppColor);
  		return true;
  	}
    return false;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if(internal.isLegal(m, color)){
  		internal.makeMove(m, color);
  		return true;
  	}
    return false;
  }
  
  /** Private function used to score a move
   * from the perspective of @scoringColor 
   */
   
  public int score(int x, int y, int color){
	  int score = 0;
	  internal.addChip(x, y, color);
	  if(internal.hasNetwork(color)){
		  score += 100000;
	  }
	  if(x==1 || x==6){
		  score += 100;
	  }
	  if(x == 2 || x == 5){
		  score += 200;
	  }
	  if(x == 3 || x == 4){
		  score += 300;
	  }
	  if(y==1 || y==6){
		  score += 100;
	  }
	  if(y == 2 || y == 5){
		  score += 200;
	  }
	  
	  if(y == 3 || y == 4){
		  score += 300;
	  }
	  if(getBlock(x,y,oppColor)){
		  score += 5000;
	  }
	  if(getBlock(x,y,color)){
		  score -= 500;
	  }
	  if(internal.neighbors(x, y, color)){
		  score -= 500;
	  }
	  if(internal.neighbors(x, y, oppColor)){
		  score += 500;
	  }
	  if(explore(x,y) == 1){
		  score += 300;
	  }
	  if(explore(x,y) == 2){
		  score += 400;
	  }
	  if(explore(x,y) == 3){
		  score += 500;
	  }
	  if(explore(x,y) == 4){
		  score += 600;
	  }
	  if(explore(x,y) > 4){
		  score += 1000;
	  }
	  if(internal.isEndGoal(internal.getContents(x, y)) && numChips >= 5){
		  score += 5000;
	  }
	  if(internal.isStartGoal(internal.getContents(x,y)) && numChips >= 5){
		  score += 5000;
	  }
	  
	  //System.out.println("Score " + score);
	  
	  
	  
	  internal.removeChip(x, y);
	  return score;
	  /*
	  Random generator = new Random();

	  return (Math.abs(generator.nextInt()%1000));
	  */
  }
  
  
  private boolean getBlock(int x, int y, int color){
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		int count7 = 0;
		int count8 = 0;
		for(int a = 1; a<9; a++){	
			switch(a){
		
				case 1: 
					for(int i=1; i<=(y); i++){
						if(internal.getContents(x, y-i)!= null)
							if(internal.getContents(x, y-i).chipColor == color){
								count1 = 1;
								break;
							}
					}
					
				case 2: 
					for(int i=1; i<=(7-y); i++){
						if(internal.getContents(x, y+i)!= null)
							if(internal.getContents(x, y+i).chipColor == color){
								count2 = 1;
								break;
							}
					}
				
				case 3: 
					for(int i=1; i<=(x); i++){
						if(internal.getContents(x-i, y) != null)
							if(internal.getContents(x-i, y).chipColor == color){
								count3 = 2;
								break;
							}
					}
				
				case 4: 
					for(int i=1; i<=(7-x); i++){
						if(internal.getContents(x+i, y) != null)
							if(internal.getContents(x+i, y).chipColor == color){
								count4 = 2;
								break;
							}
				}
				
				case 5: 
					for(int i=1; i<=(7-x); i++){
						for(int z=1; z<=(y); z++){
							
							if((x+i)>7 || (y-z)<1) break;
							if(internal.getContents(x+i, y-z) != null)
								if(internal.getContents(x+i, y-z).chipColor == color){
									count5 = 3;
									break;
								}
						}
					}
				
				case 6: 
					for(int i=1; i<=(7-x); i++){
						for(int z=1; z<=(7-y); z++){
							
							if((y+z) > 7 || (x+i) > 7) break;
							if(internal.getContents(x+i, y+z) != null)
								if(internal.getContents(x+i, y+z).chipColor == color){
									count6 = 4;
									break;
								}
						}
					}
				
				case 7: 
					for(int i=1; i<=(7-x); i++){
						for(int z=1; z<=(7-y); z++){
							
							if((y+z) > 7 || (x-i) < 1) break;
							if(internal.getContents(x-i, y+z) != null)
								if(internal.getContents(x-i, y+z).chipColor == color){
									count7 = 3;
									break;
								}
						}
					}
				
				case 8: 
					for(int i=1; i<=(7-x); i++){
						for(int z=1; z<=(y); z++){
							if((y+z) < 7 || (x-i) < 1) break;
							if(internal.getContents(x-i, y-z) != null)
								if(internal.getContents(x-i, y-z).chipColor == color){
									count8 = 4;
									break;
								}
						}
					}
				
				default:
					break;
				}
			if(count1 == count2 && count1 !=0){
				return true;
			}
			if(count3 == count4 && count3 !=0){
				return true;
			}
			if(count5 == count7 && count5 != 0){
				return true;
			}
			if(count6 == count8 && count6 != 0){
				return true;
			}
			
		}
		
		
			//return null;
		return false;
}
  private int explore(int x, int y){
	  int count = 0;
	  count = internal.getAllNeighbors(x, y);
	  
	  return count;
  }
  
  /** addMoveScore() returns the score resulting from adding a chip
   * at the given location
   */
	private MoveScore addMoveScore(int color){
		int maxScore = -10000;
		int x = -1;
		int y = -1;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				
				//System.out.println("Checking: ("+i+","+j+")");
				Move tempMove = new Move(i, j);	
								
				if(internal.isLegal(tempMove, color)){
					
					int temp = score(i, j, color);
					if(temp > maxScore){
						maxScore = temp;
						x = i;
						y = j;
						//System.out.println("X and Y set to: "+x+","+y+")");
					}	
				}
			}
	
		}
		//System.out.println("Score picked " + maxScore);
		return new MoveScore(x, y, maxScore);
	}
	private MoveScore addMoveScore(int a, int b, int color){
		Move returnMove = null;
		int maxScore = -10000;
		int x = -1;
		int y = -1;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
									//Do something about vvvv
				if (a == i && b == j) continue;
				returnMove = new Move(i, j);					
				if (internal.BoardSize[i][j] == null && internal.isLegal(returnMove, color)){
					int temp = score(i, j, color);
					if(temp > maxScore){
						maxScore = temp;
						x = i;
						y = j;
					}
					
				}
			}
	
		}
		//System.out.println("Score picked " + maxScore);
		return new MoveScore(x, y, maxScore);
	}
	
	/** stepMoveScore() returns the score resulting from adding a chip
   * at the given location and moving it somewhere else
   */
	private Move stepMoveScore(){
		int maxScore = -10000;
		int x = -1;
		int y = -1;
		int x1 = -1;
		int y1 = -1;
		MoveScore ourMove = null;
		MoveScore theirMove = null;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				

				if(internal.BoardSize[i][j] != null && internal.BoardSize[i][j].chipColor == color){
						Chip temp = internal.BoardSize[i][j];
						internal.BoardSize[i][j] = null;
						if(internal.hasNetwork(oppColor)){
							internal.BoardSize[i][j] = temp;
							continue;
						}
						ourMove = addMoveScore(i,j, color);
						internal.addChip(ourMove.x1,ourMove.y1,color);


						theirMove = addMoveScore(oppColor); 
						internal.BoardSize[i][j] = temp;
						internal.removeChip(ourMove.x1, ourMove.y1);
						
				}
				if(ourMove != null && theirMove != null){
					if(maxScore < (ourMove.score-theirMove.score)){
						maxScore = (ourMove.score-theirMove.score);
						x = i;
						y = j;
						x1 = ourMove.x1;
						y1 = ourMove.y1;
						//System.out.println("Our Move " + ourMove.x1 + ourMove.y1 + x + y);
					}
					
				}
				
			}
		}
		//System.out.println("Score picked " + maxScore);
		return new Move(x1, y1,x,y);
	}
	
	/** returns a random legal move to break stalemates
	 * 
	 */
	 
	private Move staleMove(){
		Random generator = new Random();
		while(true){
			int x = Math.abs(generator.nextInt())%7;
			int y = Math.abs(generator.nextInt())%7;
			Move random = new Move(x, y, lastMovesMade[2].x2, lastMovesMade[2].y2);
			if(internal.isLegal(random, color)){
				return random;
			}
		}
	}
	
	/** returns @true if a stalemate has been 
	 *  reached
	 */
	private boolean staleMate(Move m){
		
		if(lastMovesMade[1] == null)return false;
		if(lastMovesMade[1].x1 == m.x1 && lastMovesMade[1].y1 == m.y1){
			return true;
		}
		return false;
	}

	private void updateMoves(Move m){
		Move tempArray[] = new Move[3];
		tempArray[2] = lastMovesMade[1];
		tempArray[1] = lastMovesMade[0];
		tempArray[0] = m;
		lastMovesMade = tempArray;
	}
	
	public static void main(String [] args){
		Board test = new Board();
		MachinePlayer test2 = new MachinePlayer(WHITE);
		test2.chooseMove();
		test2.internal.printBoard();
		test2.chooseMove();
		test2.internal.printBoard();
	}

}
