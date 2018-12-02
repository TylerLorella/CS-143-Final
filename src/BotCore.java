import java.util.Arrays;

public class BotCore {
	
	//**our basic implementation should try to use our boardRate method to determine a better than random move.

	//need core turn mechanics? or will the other groups work on this aspect?
		//perhaps instead add a variable (int playerOne) to methods so that the methods can be used for any team
			//would need to be able to interept what that means for what the integers on the board equals
	
	//need to generate a list of possible moves that can be made?
	
	//need some way to rate each move
		//might be best to have the board state be used, ie a class that scans the board and gives it a rating
			//maybe +2 for each normal ai piece, +4 for each ai king piece
			//-2 for each enemy normal piece, -4 for each enemy king piece
		//would want to check if the enemy could grab one of our pieces or multiple? (upgrade)
	
	//need some recursion mechanics to find all possibe moves at depth X
		//need to maximize my points
		//need to minimize opponent pieces
	
	private String findMove(int[][] board, boolean playerOne, int depth) {
		//int playerOne = 1 is the human controlled, playerOne = 2 is bot controlled
		//scan through board to find a possible move for the bot
		//determine the board made from that possible move and call findMove on that board
			//again, find all possible moves, repeat until the desired depth or time has been reached
		//int[][] boardClone = board.clone(); //build a clone of our board to play with
		
		//"temp" variables to store the move that resulted in the best case
		int oldXCord = 0; //the variables to store the location of the piece that we want to move
		int oldYCord = 0;
		int newXCord = 0; //the variables to store the new location of the moved piece
		int newYCord = 0;
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if (playerOne == true) { //if we're trying to make an optimal move for player one
					if (board[x][y] == 1) { //checking to see if we have found a player one space
						//try the different possible moves and return the value of the method
						//
					}
				}
			}
		}
		
	}
	
	private int tryMove(int[][] board, int oldXCord, int oldYCord, int newXCord, int newYCord, boolean playerOne, int depth) {
		//look at the coordinates and see if there is a possible move
			//if there is then check the
	}
	
	
	
	
	/**
	 *  A method to determine how good the board state is for the given board and player. A positve returned integer indicates
	 *  the board is more favorable for the given playerOne while a negative integer indicates less favorable.
	 *  Currently, the method only counts the number of pieces each player has and uses that to determine the board rating.
	 * @param board The board to be analyzed and given an integer value on it's usefulness
	 * @paran playerOne Which player the board is being analyzed for; a value of 1 corresponds to player 1
	 * @return An integer is returned, positive is favorable, negative is not.
	 */
	private int boardRate(int[][] board, boolean playerOne) {
		
		int boardScore = 0;
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				//a loop that looks at all spots on the board and uses the below if statements to adjust the score
				//if the value on the board is (-2: Enemy king), (-1: Enemy normal), (1: My Normal), (2: My King)
				
				if (board[y][x] == 0);// do nothing currently (to remove the chance it's an unused spot)
				else if (board[y][x] == 1) boardScore = boardScore + 2;
				else if (board[y][x] == -1) boardScore = boardScore - 2;
				else if (board[y][x] == 2) boardScore = boardScore + 4;
				else if (board[y][x] == -2) boardScore = boardScore - 4;
			}
		}
		
	if (playerOne == true) return boardScore;
	else return -boardScore;
	}
	
	
	
}
