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
  public int searchDepth;
  private Board internal;
  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
  	this(color, 1);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
  	internal = new Board();
  	this.color = color;
  	this.searchDepth = searchDepth;
  	if(color==WHITE){
  		oppColor = BLACK;
  	}else{
  		oppColor = WHITE;
  	}
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  
	  if(internal.totalChipCount() < 20){
		Move chosenMove = (Move) addMoveScore(color);
		internal.makeMove(chosenMove,color);
		internal.printBoard();
		return chosenMove;
	  }else{
			Move chosenMove = stepMoveScore(color);
			internal.makeMove(chosenMove, color);
			return chosenMove;
	  }
  } 
  /*
  public Move chooseMove(){
	  return recursiveChooser(this.searchDepth);
  }
  
  public Move recursiveChooser(int searchDepth){
	  
	  Move bestMove = null;
	  Move theirBestMove = null;
	  
	  if(internal.totalChipCount() < 11){
		  
		bestMove = (Move) addMoveScore(color);
	  
	  }else{
		bestMove = stepMoveScore(color);
	  }
	  
	  internal.makeMove(bestMove, color);
	  
	  // If you have searched deep enough, delete the move and return
	  if(searchDepth < 1){
		  internal.unmakeMove(bestMove);
		  System.out.println("Returning bestMove");
		  return bestMove;
	  }
	  // Othewise, check the opponents best move, make it, and recurse
	  else{
		  
		if(internal.totalChipCount() < 11){
		  
			theirBestMove = (Move) addMoveScore(oppColor);
	  
		}else{
			theirBestMove = stepMoveScore(oppColor);
		}
	  
		internal.makeMove(theirBestMove, oppColor);
		bestMove = recursiveChooser(searchDepth--);
	  }
	  internal.unmakeMove(theirBestMove);
	  return bestMove;
  } */
		  

/*
	public Move chooseMove(){
		Random generator = new Random();
		int cordX = Math.abs(generator.nextInt()%8);
		int cordY = Math.abs(generator.nextInt()%8);
		System.out.println("Accessing: ("+cordX+","+cordY+")");
		while(true){
			
			Move temp = new Move(cordX, cordY);
			if(internal.isLegal(temp, color)){
				internal.makeMove(temp, color);
				return temp;
			}else{
			cordX = Math.abs(generator.nextInt()%8);
			cordY = Math.abs(generator.nextInt()%8);
			System.out.println(color+" Accessing: ("+cordX+","+cordY+")");
		}
		}	
	}
*/
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
	  
	  System.out.println("Score " + score);
	  
	  
	  
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
					
					int temp = score(internal.getContents(i, j));
					if(temp > maxScore){
						maxScore = temp;
						x = i;
						y = j;
						//System.out.println("X and Y set to: "+x+","+y+")");
					}	
				}
			}
	
		}
		
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
					int temp = score(internal.getContents(i, j));
					if(temp > maxScore){
						maxScore = temp;
						x = i;
						y = j;
					}
					
				}
			}
	
		}
		return new MoveScore(x, y, maxScore);
	}
	
	/** stepMoveScore() returns the score resulting from adding a chip
   * at the given location and moving it somewhere else
   */
	private Move stepMoveScore(int color){
		int tempOpponent = opponentColorFinder(color);
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
						if(internal.hasNetwork(tempOpponent)){
							internal.BoardSize[i][j] = temp;
							continue;
						}
						ourMove = addMoveScore(i,j, color);
						internal.addChip(ourMove.x1,ourMove.y1,color);


						theirMove = addMoveScore(tempOpponent); 
						internal.BoardSize[i][j] = temp;
						internal.removeChip(ourMove.x1, ourMove.y1);
						internal.printBoard();
						
				}
				if(ourMove != null && theirMove != null){
					if(maxScore < (ourMove.score-theirMove.score)){
						maxScore = (ourMove.score-theirMove.score);
						x = i;
						y = j;
						x1 = ourMove.x1;
						y1 = ourMove.y1;
					}
					
				}
				
			}
		}
		return new Move(x1, y1,x,y);
	}
	
	private int opponentColorFinder(int color){
		if(color == WHITE){
			return BLACK;
		}
		return WHITE;
	}
	

	
  /*
  public static void main (String[] args){
	Board test = new Board();
	System.out.println(test.getContents(0,0));
	System.out.println(test.getContents(7,7));
	test.addChip(0,0,BLACK);
	test.addChip(7,7,WHITE);
	System.out.println(test.getContents(0,0).chipColor);
	System.out.println(test.getContents(7,7).chipColor);
	test.addChip(1,1,BLACK);
	test.addChip(1,2,WHITE);
	System.out.println(test.number(BLACK));
	System.out.println(test.number(WHITE)); // tested through number
	System.out.println(test.touched(0,0)); // used to make sure if one chip is placed and says touched
	System.out.println(test.touched(1,1)); // that the other chip is also changed to touched.
	System.out.println(test.touched(1,2));
	System.out.println(test.touched(7,7)); 
	Move practice = new Move(0,1);
	System.out.println("Tested " + test.isLegal(practice, BLACK));
	Move practice1 = new Move(6,6);
	System.out.println("Testing this one " + test.isLegal(practice1, WHITE));
	Move practice2 = new Move(1,0);
	System.out.println(test.isLegal(practice2, BLACK));
	
	Move practice10 = new Move(0,2);
	Move practice11 = new Move(2,2);
	Move practice12 = new Move(2,4);
	Move practice13 = new Move(4,4);
	Move practice14 = new Move(4,2);
	Move practice15 = new Move(7,2);
	
	test = new Board();
	
	test.makeMove(practice10, WHITE);
	test.makeMove(practice11, WHITE);
	test.makeMove(practice12, WHITE);
	test.makeMove(practice13, WHITE);
	test.makeMove(practice14, WHITE);
	test.makeMove(practice15, WHITE);
	
	if(test.hasNetwork(WHITE)){
		System.out.println("working");
	}else{
		System.out.println("broken");
  	}

  }A*/

}
