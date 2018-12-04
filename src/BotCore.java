
import java.util.SortedMap;
import java.util.TreeMap;

public class BotCore {
	//x refers to the row while y refers to the column
	SortedMap<Integer, int[][]> possibleBoardsJ = new TreeMap<Integer, int[][]>(); //a list of the possible jumps that can be made
	SortedMap<Integer, int[][]> possibleBoardsM = new TreeMap<Integer, int[][]>(); //a list of the possible moves that can be made if there are no jumps yet
	
	//**our basic implementation should try to use our boardRate method to determine a better than random move.

	//need core turn mechanics? or will the other groups work on this aspect?
	//perhaps instead add a variable (int playerOne) to methods so that the methods can be used for any team
	//would need to be able to interept what that means for what the integers on the board equals

	//need to generate a list of possible moves that can be made

	//need some way to rate each move
	//might be best to have the board state be used, ie a class that scans the board and gives it a rating
	//maybe +2 for each normal ai piece, +4 for each ai king piece
	//-2 for each enemy normal piece, -4 for each enemy king piece
	//would want to check if the enemy could grab one of our pieces or multiple? (upgrade)

	//need some recursion mechanics to find all possibe moves at depth X
	//need to maximize my points
	//need to minimize opponent pieces

	
	/**
	 * This method will look at the passed through board state and decide what actions are possible.
	 * if a jump is detected the method will rate each jump based on the method boardRate, the score is determined by the number of pieces each side has, so the jump 
	 * that leaves the highest number of pieces for the specified player will be scored the best. If not possible jump action can be made the method will return a random
	 * move action that can be made. 
	 * @param board the current board state
	 * @param playerOne if this method is trying to find the best rated action for player one, false will return the bots best rated actionn
	 * @param depth currently not implemented, this value is not used
	 * @throws Exception if no possible action was detected it will throw an error
	 */
	public void TakeTurn(int[][] board, boolean playerOne, int depth) throws Exception {
		//int playerOne = 1 is the human controlled, playerOne = 2 is bot controlled
		//scan through board to find a possible move for the bot
		//determine the board made from that possible move and call a movement method on that board
		//again, find all possible moves, repeat until the desired depth or time has been reached
		//int[][] boardClone = board.clone(); //build a clone of our board to play with

		boolean jumpable = false;
		boolean moveable = false;

		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {

				if (tryJump(board, x, y, false) == true) { 	//check if any piece can do a jump, if we return true then there is a jump we have to do!!
					jumpable = true;
				} else if (jumpable == false) { //only want to check for a movement if there was/has been no movements
					if (moveable(board, x, y, board[x][y], false) == true) moveable = true;
				}
			}
		}

		if (jumpable == true) {
			//we want to see if any of the pieces could jump
			int bestScore = possibleBoardsJ.lastKey(); //used a sorted map so the highest score (key) should be the last entry
			board = possibleBoardsJ.get(bestScore);
			possibleBoardsJ.clear();
			possibleBoardsM.clear(); //may still of been a move detected before any jumps were found
			return;
		} else if (moveable == true) {
			//if there were no jumps then our possibleBoardsJ will contain 
			// we will randomly choose a board from our inventory of possibleBoardsM, because our current board rating method only considers the number of pieces
			int random = (int) (Math.random() * possibleBoardsM.size()); //we will randomly choose an integer of value between 0 and the size of possibleBoardsM
			board = possibleBoardsM.get(random);
			possibleBoardsM.clear();
			return;
		}
		//bad territory, no movements or jumps were detected
		throw new Exception("No moves or jumps were detected");
	}
	
	/**
	 *  A method to check if the piece at the coordates passed through is capable of movement and will return true if it's possible in addition to adding a possible board.
	 * @param board the current board state
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @param pieceValue the value of the piece to look at
	 * @param playerOne if the method is searching for human player moves
	 * @return returns true if the piece is moveable and will add the resulting movement to the list of possibleBoardsM
	 */
	private boolean moveable(int[][] board, int x, int y, int pieceValue, boolean playerOne) {
	boolean moveable = false;
		if (playerOne == false) {
			if (board[x][y] == -1 || board[x][y] == -2) {
				if (validPosition(board, x-1, y+1) == true && board[x-1][y+1] == 0) {
					int[][] pBoard = new int[board.length][board[0].length];
					boardCopy(board, pBoard);
					pBoard[x][y] = 0;
					pBoard[x-1][y+1] = pieceValue;
					int nextLastKey = 0;
					if (possibleBoardsM.isEmpty() == false) nextLastKey = possibleBoardsM.lastKey()+1;
					possibleBoardsM.put(nextLastKey, pBoard);
					moveable = true;
				}
				
				if (validPosition(board, x+1, y+1) == true && board[x+1][y+1] == 0) {
					int[][] pBoard = new int[board.length][board[0].length];
					boardCopy(board, pBoard);
					pBoard[x][y] = 0;
					pBoard[x+1][y+1] = pieceValue;
					int nextLastKey = 0;
					if (possibleBoardsM.isEmpty() == false) nextLastKey = possibleBoardsM.lastKey()+1;
					possibleBoardsM.put(nextLastKey, pBoard);
					moveable = true;
				}
			}
			
			if (board[x][y] == -2) {
				if (validPosition(board, x-1, y-1) == true && board[x-1][y-1] == 0) {
					int[][] pBoard = new int[board.length][board[0].length];
					boardCopy(board, pBoard);
					pBoard[x][y] = 0;
					pBoard[x-1][y-1] = pieceValue;
					int nextLastKey = 0;
					if (possibleBoardsM.isEmpty() == false) nextLastKey = possibleBoardsM.lastKey()+1;
					possibleBoardsM.put(nextLastKey, pBoard);
					moveable = true;
				}
				
				if (validPosition(board, x+1, y+1) == true && board[x+1][y+1] == 0) {
					int[][] pBoard = new int[board.length][board[0].length];
					boardCopy(board, pBoard);
					pBoard[x][y] = 0;
					pBoard[x+1][y+1] = pieceValue;
					int nextLastKey = 0;
					if (possibleBoardsM.isEmpty() == false) nextLastKey = possibleBoardsM.lastKey()+1;
					possibleBoardsM.put(nextLastKey, pBoard);
					moveable = true;
				}
			}
		}
		return false;
	}

	/**
	 * A method to check if the coordinates being passed through are legal coordinates and have not entered the FORBIDDEN ZONE
	 * @param board the game board to be passed through
	 * @param x the column coordinate
	 * @param y the row coordinate
	 * @return returns true if the location passed through is valid
	 */
	private boolean validPosition(int[][] board, int x, int y) {
		if (x < board.length && x >= 0) {
			if (y < board[0].length && y >= 0) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * A method that will attempt to search if the piece and coordinates passed through is capable of making a jump, returns a boolean if a jump was found,
	 * and it will add the resulting board state into a list of rated jumps
	 * @param board the current board state
	 * @param x the row coordinates
	 * @param y the column coordinates
	 * @param playerOne if this method is working for player one
	 * @return returns a boolean, true if a jump was found and false if not
	 */
	private boolean tryJump(int[][] board, int x, int y, boolean playerOne) {
		boolean jumpable = false;

		//********************************************************************************************* Assuming that the bot is starting in the upper section of the board
		//different possible jumps for a normal piece and a king piece
		if (playerOne == false) { //looking to make a jump for the bot

			if (board[x][y] == -1 || board[x][y] == -2) {
				//we have found a normal piece so we want to check for a jump to the left or right

				if (validPosition(board, x-1, y-1) == true && (board[x-1][y-1] == 1 || board[x-1][y-1] == 2)) { //there is a piece to our mid left
					if (validPosition(board, x-2, y-2) == true && board[x-2][y-2] == 0) {//there is an empty space bottom left
						jumpable = true; //there is a available jump!
						doJump(board, x, y, x-1, y-1, x-2, y-2, board[x][y], false);
					}
					if (validPosition(board, x, y-2) == true && board[x][y-2] == 0) { //there is an empty space bottom mid
						jumpable = true; //there is a available jump!
						doJump(board, x, y, x-1, y-1, x, y-2, board[x][y], false);
					}
				}

				if (validPosition(board, x+1, y-1) == true && (board[x+1][y-1] == 1 || board[x+1][y-1] == 2) ) { //there is a piece to our mid right
					if (validPosition(board, x+2, y-2) == true && board[x+2][y-2] == 0) {//there is an empty space bottom right
						jumpable = true; //there is a available jump!
						doJump(board, x, y, x+1, y-1, x+2, y-2, board[x][y], false);
					}
					if (validPosition(board, x, y-2) == true && board[x][y-2] == 0) { //there is an empty space bottom mid
						jumpable = true; //there is a available jump!
						doJump(board, x, y, x+1, y-1, x, y-2, board[x][y], false);
					}
				}

				//     0   1  2   3   4  
				//	7[TL][  ][TM][  ][TR]
				//	6[  ][1 ][  ][ 1][  ]
				//	5[  ][  ][-2][  ][  ]
				if (board[x][y] == -2) { //the special moves reserved for king pieces
					if (validPosition(board, x-1, y+1) == true && (board[x-1][y+1] == 1 || board[x-1][y+1] == 2)) { //there is a piece to our mid left
						if (validPosition(board, x-2, y+2) == true && board[x-2][y+2] == 0) {//there is an empty space top left
							jumpable = true; //there is a available jump!
							doJump(board, x, y, x-1, y+1, x-2, y+2, board[x][y], false);
						}
						if (validPosition(board, x, y+2) == true && board[x][y+2] == 0) { //there is an empty space top mid
							jumpable = true; //there is a available jump!
							doJump(board, x, y, x-1, y+1, x, y+2, board[x][y], false);
						}
					}

					if (validPosition(board, x+1, y+1) == true && (board[x+1][y+1] == 1 || board[x+1][y+1] == 2) ) { //there is a piece to our mid right
						if (validPosition(board, x+2, y+2) == true && board[x+2][y+2] == 0) {//there is an empty space top right
							jumpable = true; //there is a available jump!
							doJump(board, x, y, x+1, y+1, x+2, y+2, board[x][y], false);
						}
						if (validPosition(board, x, y+2) == true && board[x][y+2] == 0) { //there is an empty space top mid
							jumpable = true; //there is a available jump!
							doJump(board, x, y, x+1, y+1, x, y+2, board[x][y], false);
						}
					}
				}
			}
		}

		return jumpable;
	}

	/**
	 * A method to check if there are jumps available and creates a new possible board in our board collections, returns true if a jump was found
	 * @param board the base board that is used to determine if there are other jumps possibles
	 * @param oldX The initial row coordinates
	 * @param oldY The initial column coordinates
	 * @param jumpX The piece that is being jumped over
	 * @param jumpY
	 * @param newX
	 * @param newY
	 * @param pieceValue
	 * @param playerOne if true then this method is trying to find the jumps that playerOne can do, not currently implemented fully
	 */
	private void doJump(int[][] board, int oldX, int oldY, int jumpX, int jumpY, int newX, int newY, int pieceValue, boolean playerOne) {
		if (playerOne == false) { //the bots turn
			int[][] pBoard = new int[board.length][board[0].length];
			boardCopy(board, pBoard);
			pBoard[oldX][oldY] = 0;
			pBoard[jumpX][jumpY] = 0;
			pBoard[newX][newY] = pieceValue;
			boolean temp = tryJump(pBoard, newX, newY, false);
			if (temp != true) { //if there is no chain jump then we want to add our current position as a possible move
				possibleBoardsJ.put(boardRate(pBoard, false), pBoard);
			}
		} else { //the "players" turn
			int[][] pBoard = new int[board.length][board[0].length];
			boardCopy(board, pBoard);
			pBoard[oldX][oldY] = 0;
			pBoard[jumpX][jumpY] = 0;
			pBoard[newX][newY] = pieceValue;
			boolean temp = tryJump(pBoard, newX, newY, false);
			if (temp != true) { //if there is no chain jump then we want to add our current position as a possible move
				possibleBoardsJ.put(boardRate(pBoard, false), pBoard);
			}
		}

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

	/**
	 * This method is used to make a copy of int[][] a onto int[][] b
	 * @param a the 2D array to copy
	 * @param b the 2D array to copy on
	 */
	private void boardCopy(int[][] a, int[][] b) {
		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < a[0].length; y++) {
				b[x][y] = a[x][y];
			}
		}
	}



}
