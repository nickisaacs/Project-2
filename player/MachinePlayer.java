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
    return new Move();
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

}
