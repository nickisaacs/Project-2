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
  private int numChips;
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
	  
	  if(numChips < 10){
		Move chosenMove = (Move) addMoveScore(this.color);
		internal.makeMove(chosenMove,color);
		internal.printBoard();
		numChips++;
		return chosenMove;
	  }else{
			Move chosenMove = stepMoveScore();
			internal.makeMove(chosenMove, color);
			return chosenMove;
	  }
  } 

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
   
  public int score(Chip c){
	  Random generator = new Random();

	  return (Math.abs(generator.nextInt()%1000));
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
	private Move stepMoveScore(){
		int maxScore = -10000;
		int x = -1;
		int y = -1;
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
						
				}
				
				if(maxScore < (ourMove.score-theirMove.score)){
					maxScore = (ourMove.score-theirMove.score);
				}
				internal.removeChip(ourMove.x1, ourMove.y1);
			}
		}
		return new Move(x, y);
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
