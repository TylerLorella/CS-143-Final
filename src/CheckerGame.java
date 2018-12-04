public class CheckerGame {

	public static int w = 1000, h = 1000; // width and height.
	public static final int EMPTY = 0, RED = 1, RED_KING = 2, WHITE = 3, WHITE_KING = 4; // pieces data
	public static final int tSize = w / 8; // Tiles size.
	public static final int numOfTiles = 8; // # of tiles per row is 8.
	public static int[][] boardData = new int[8][8]; // 8x8 board.
	public static int[][] pieceData = new int[8][8]; // stores piece data in a board.
	public static boolean gameIn = true; // game in progress.
	public static int playerIn = RED; // game starts with RED player first.
	public static boolean movePlay = false; // Is there a move function processing?
	public static int[][] plays = new int[8][8]; // Store available plays.
	public int sRow, sCol; // Stored row and col data
	public boolean jump = false;



	public void pieceMove(int col, int row, int savedCol, int savedRow) {
		int i = pieceData[savedCol][savedRow]; // change the piece to new tile
		pieceData[col][row] = i;
		pieceData[savedCol][savedRow] = EMPTY; // change old piece location to EMPTY
		checkKing(col, row);
		if (jump == true)
			deletePiece(col, row, savedCol, savedRow);
		restart();
		swap();
	}

	public void swap() { // Swap Players
		if (playerIn == RED)
			playerIn = WHITE;
		else
			playerIn = RED;
	}



	public void deletePiece(int col, int row, int sCol, int sRow) { // col, row, stored col, store row.
		int pRow = -1;
		int pCol = -1;
		if (col > sCol && row > sRow) {
			pRow = row - 1;
			pCol = col - 1;
		}
		if (col > sCol && row < sRow) {
			pRow = row + 1;
			pCol = col - 1;
		}
		if (col < sCol && row > sRow) {
			pRow = row - 1;
			pCol = col + 1;
		}
		if (col < sCol && row < sRow) {
			pRow = row + 1;
			pCol = col + 1;
		}
		pieceData[pCol][pRow] = EMPTY;
	}// TODO REWRITE



	// Mouse presses code.

	public void mClicked(java.awt.event.MouseEvent evt) {
		int col = (evt.getX() - 8) / tSize; // 8 is left frame length
		int row = (evt.getY() - 30) / tSize; // 30 is top frame length
		if (movePlay == false && pieceData[col][row] != 0 || movePlay == true && teamPiece(col, row) == true) {
			restart();
			sCol = col;
			sRow = row; // Sets the current click to instance variables to be used elsewhere
			availablePlays(col, row);
		} else if (movePlay == true && plays[col][row] == 1) {
			pieceMove(col, row, sCol, sRow);
		} else if (movePlay == true && plays[col][row] == 0) {
			restart();
		}
	}

	// Game over code portion.
	public boolean gameOver() {

		return gameOverData(0, 0, 0, 0);
	}

	public boolean gameOverData(int col, int row, int red, int white) {

		if (pieceData[col][row] == RED || pieceData[col][row] == RED_KING)
			red += 1;
		if (pieceData[col][row] == WHITE || pieceData[col][row] == WHITE_KING)
			white += 1;
		if (col == numOfTiles - 1 && row == numOfTiles - 1) {
			if (red == 0 || white == 0)
				return true;
			else
				return false;
		}
		if (col == numOfTiles - 1) {
			row += 1;
			col += -1;
		}
		return gameOverData(col + 1, row, red, white);
	}

	// Restart Play Code.

	public void restart() {
		sCol = 0;
		sRow = 0;
		movePlay = false;
		jump = false;
		for (int row = 0; row < numOfTiles; row++) {
			for (int col = 0; col < numOfTiles; col++) {
				plays[col][row] = 0;
			}
		}
	}



	public boolean king(int col, int row) {
		if (pieceData[col][row] == RED_KING || pieceData[col][row] == WHITE_KING) {
			return true;
		} else
			return false;
	}

	public int opponent(int col, int row) { // check opponent
		if (pieceData[col][row] == RED || pieceData[col][row] == RED_KING)
			return WHITE;
		else
			return RED;
	}

	public void checkKing(int col, int row) {
		if (pieceData[col][row] == RED && row == 0)
			pieceData[col][row] = RED_KING;
		else if (pieceData[col][row] == WHITE && row == numOfTiles - 1)
			pieceData[col][row] = WHITE_KING;
		else
			return;
	}

	public void availablePlays(int col, int row) {
		movePlay = true;
		if ((teamPiece(col, row) == true)) { // checks if the piece is assigned to the current player
			if (pieceData[col][row] == RED) { // only goes north, checks the row above it's own
				availableUp(col, row);
			}
			if (pieceData[col][row] == WHITE) { // only goes south, checks the row below it's own
				availableDown(col, row);
			}
			if (pieceData[col][row] == RED_KING || pieceData[col][row] == WHITE_KING) { // Goes up OR down 1 row below
				// it's own
				availableUp(col, row);
				availableDown(col, row); // GET UP GET UP AND GET DOWN
			}

		}
	}

	public void availableUp(int col, int row) { // Get Up availability
		int rUp = row - 1; // row up
		if (col == 0 && row != 0) {
			for (int i = col; i < col + 2; i++) { // check to right
				if (pieceData[col][row] != 0 && pieceData[i][rUp] != 0) {
					if (ableToJump(col, row, i, rUp) == true) {
						int jumpCol = jumpPos(col, row, i, rUp)[0];
						int jumpRow = jumpPos(col, row, i, rUp)[1];
						plays[jumpCol][jumpRow] = 1;
					}
				} else if (boardData[i][rUp] == 1 && pieceData[i][rUp] == 0)
					plays[i][rUp] = 1;
			}
		} else if (col == (numOfTiles - 1) && row != 0) { // X=max, Y is not 0
			if (pieceData[col][row] != 0 && pieceData[col - 1][rUp] != 0) {
				if (ableToJump(col, row, col - 1, rUp) == true) {
					int jumpCol = jumpPos(col, row, col - 1, rUp)[0];
					int jumpRow = jumpPos(col, row, col - 1, rUp)[1];
					plays[jumpCol][jumpRow] = 1;
				}
			} else if (boardData[col - 1][rUp] == 1 && pieceData[col - 1][rUp] == 0)
				plays[col - 1][rUp] = 1;
		} else if (col != numOfTiles - 1 && col != 0 && row != 0) {
			for (int i = col - 1; i <= col + 1; i++) {
				if (pieceData[col][row] != 0 && pieceData[i][rUp] != 0) {
					if (ableToJump(col, row, i, rUp) == true) {
						int jumpCol = jumpPos(col, row, i, rUp)[0];
						int jumpRow = jumpPos(col, row, i, rUp)[1];
						plays[jumpCol][jumpRow] = 1;
					}
				} else if (boardData[i][rUp] == 1 && pieceData[i][rUp] == 0)
					plays[i][rUp] = 1;
			}
		}
	}

	public void availableDown(int col, int row) {
		int rDown = row + 1;
		if (col == 0 && row != numOfTiles - 1) {
			if (pieceData[col][row] != 0 && pieceData[col + 1][rDown] != 0) {
				if (ableToJump(col, row, col + 1, rDown) == true) {
					int jumpCol = jumpPos(col, row, col + 1, rDown)[0];
					int jumpRow = jumpPos(col, row, col + 1, rDown)[1];
					plays[jumpCol][jumpRow] = 1;
				}
			} else if (boardData[col + 1][rDown] == 1 && pieceData[col + 1][rDown] == 0)
				plays[col + 1][rDown] = 1;
		} else if (col == numOfTiles - 1 && row != numOfTiles - 1) {
			if (pieceData[col][row] != 0 && pieceData[col - 1][rDown] != 0) {
				if (ableToJump(col, row, col - 1, rDown) == true) {
					int jumpC = jumpPos(col, row, col - 1, rDown)[0];
					int jumpR = jumpPos(col, row, col - 1, rDown)[1];
					plays[jumpC][jumpR] = 1;
				}
			} else if (boardData[col - 1][rDown] == 1 && pieceData[col - 1][rDown] == 0)
				plays[col - 1][rDown] = 1;
		} else if (col != numOfTiles - 1 && col != 0 && row != numOfTiles - 1) {
			for (int i = col - 1; i <= col + 1; i++) {
				if (pieceData[col][row] != 0 && pieceData[i][rDown] != 0) {
					if (ableToJump(col, row, i, rDown) == true) {
						int jumpCol = jumpPos(col, row, i, rDown)[0];
						int jumpRow = jumpPos(col, row, i, rDown)[1];
						plays[jumpCol][jumpRow] = 1;
					}
				} else if (boardData[i][rDown] == 1 && pieceData[i][rDown] == 0)
					plays[i][rDown] = 1;
			}
		}
	}

	public boolean teamPiece(int col, int row) { // check team piece
		if (playerIn == RED && (pieceData[col][row] == RED || pieceData[col][row] == RED_KING)) // bottom
			return true;
		if (playerIn == WHITE && (pieceData[col][row] == WHITE || pieceData[col][row] == WHITE_KING)) // top
			return true;
		else
			return false;
	}

	public boolean legalPos(int col, int row) {
		if (row < 0 || row >= numOfTiles || col < 0 || col >= numOfTiles)
			return false;
		else
			return true;
	}

	public boolean ableToJump(int col, int row, int player2Col, int player2Row) {

		if (((pieceData[col][row] == WHITE || pieceData[col][row] == WHITE_KING)
				&& (pieceData[player2Col][player2Row] == RED || pieceData[player2Col][player2Row] == RED_KING))
				|| (pieceData[col][row] == RED || pieceData[col][row] == RED_KING)
				&& (pieceData[player2Col][player2Row] == WHITE
				|| pieceData[player2Col][player2Row] == WHITE_KING)) {

			if (player2Col == 0 || player2Col == numOfTiles - 1 || player2Row == 0 || player2Row == numOfTiles - 1)
				return false;
			int[] toData = jumpPos(col, row, player2Col, player2Row);
			if (legalPos(toData[0], toData[1]) == false)
				return false;
			if (pieceData[toData[0]][toData[1]] == 0) {
				jump = true;
				return true;
			}
		}
		return false;
	}

	public int[] jumpPos(int col, int row, int player2Col, int player2Row) {
		if (col > player2Col && row > player2Row && pieceData[col - 2][row - 2] == 0)
			return new int[] { col - 2, row - 2 };
		else if (col > player2Col && row < player2Row && pieceData[col - 2][row + 2] == 0)
			return new int[] { col - 2, row + 2 };
		else if (col < player2Col && row > player2Row && pieceData[col + 2][row - 2] == 0)
			return new int[] { col + 2, row - 2 };
		else
			return new int[] { col + 2, row + 2 };
	}

}