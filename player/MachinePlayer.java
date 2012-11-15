/* MachinePlayer.java */

package player;

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
  	this(color, -1);
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
	  int max = -10000;
	  int cordX = 0;
	  int cordY = 0;
	  if(internal.number(color) == 10){ // FIXME: how many till step move
		  for(int x = 0; x<7; x++){
			  for(int y = 0; y<7; y++){
				  Move temp = new Move(x,y);
				  if(internal.isLegal(temp, color)){
					  int temp2 = score();
					  if(temp2>max){
						  max = temp2;
						  cordX = x;
						  cordY = y;
					  }
				  }
			  }
		  }
	  }
	  else{
		  for(int x = 0; x<7; x++){
			  for(int y = 0; y<7; y++){
				  Move temp = new Move(x,y);
				  if(internal.isLegal(temp, color)){
					  int temp2 = score();
					  if(temp2>max){
						  max = temp2;
						  cordX = x;
						  cordY = y;
					  }
				  }
			  }
		  }
	  }
    return new Move(cordX, cordY);
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
  
  public int score(){
	  int score = 0;
	  if(internal.hasNetwork(color)){
		  score += 1000;
	  }
	  return score;
  }
  
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

  }
}