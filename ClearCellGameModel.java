package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 * @author Dept of Computer Science, UMCP
 */

public class ClearCellGameModel extends GameModel {
	
	/* Include whatever instance variables you think are useful. */
	private int playerScore; //the player's score
	private Random random; //the random number generator
	
	/**
	 * Defines a board with empty cells.  It relies on the
	 * super class constructor to define the board.
	 * 
	 * @param rows number of rows in board
	 * @param cols number of columns in board
	 * @param random random number generator to be used during game when
	 * rows are randomly created
	 */
	//Constructor 
	public ClearCellGameModel(int rows, int cols, Random random) {
		super(rows, cols); //call to the super class GameModel
		this.random = random; //sets the random number generator
		playerScore = 0; //declare the variable 
	}

	/**
	 * The game is over when the last row (the one with index equal
	 * to board.length-1) contains at least one cell that is not empty.
	 */
	//Tells you whether the game is over 
	public boolean isGameOver() {
		if(!rowIsEmpty(board[board.length-1])) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the player's score.  The player should be awarded one point
	 * for each cell that is cleared.
	 * 
	 * @return player's score
	 */
	public int getScore() {
		return playerScore;
	}

	
	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do 
	 * the following:
	 * 
	 * 1. Shift the existing rows down by one position.
	 * 2. Insert a row of random BoardCell objects at the top
	 * of the board. The row will be filled from left to right with cells 
	 * obtained by calling BoardCell.getNonEmptyRandomBoardCell().  (The Random
	 * number generator passed to the constructor of this class should be
	 * passed as the argument to this method call.)
	 */
	public void nextAnimationStep() {
		if(!isGameOver()) {
			//goes from the second to last row and and moves everything down 1
			for(int row = board.length-2; row >= 0; row--) {
				for(int col = 0; col < board[row].length; col++) {
					setBoardCell(row+1, col, getBoardCell(row, col)); 
				}
			}
			//goes through the first row and adds random cells
			for(int col = 0; col < board[0].length; col++) {
				setBoardCell(0, col, BoardCell.getNonEmptyRandomBoardCell(random));
			}
		}
	}

	/**
	 * This method is called when the user clicks a cell on the board.
	 * If the selected cell is not empty, it will be set to BoardCell.EMPTY, 
	 * along with any adjacent cells that are the same color as this one.  
	 * (This includes the cells above, below, to the left, to the right, and 
	 * all in all four diagonal directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of 
	 * cells then those rows will "collapse", meaning that all non-empty 
	 * rows beneath the collapsing row will shift upward. 
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for 
	 * invalid row or "Invalid column index" for invalid column.  We check 
	 * for row validity first.
	 */
	public void processCell(int rowIndex, int colIndex) {
		if(rowIndex < 0 || rowIndex > getRows()-1) {
			throw new IllegalArgumentException("Invalid row index");
		}
		if(colIndex < 0 || colIndex > getCols()-1) {
			throw new IllegalArgumentException("Invalid column index");
		}
		
		/* These positions are the cells to the left, right, above, and below the user
		  selected cell */
		int left = colIndex-1, right = colIndex+1, above = rowIndex-1, below = rowIndex+1;
		
		BoardCell userSelected = board[rowIndex][colIndex]; //the cell pressed by the user
		
		/* checks if the player selected a non-empty cell and makes it empty, along with cells
	 	that are the same as the selected cell. It than adds 1 point for each one cleared */
		if(userSelected != BoardCell.EMPTY) {
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			playerScore++;
			if(!(left < 0)) {
				if(userSelected == board[rowIndex][left]) {
					board[rowIndex][left] = BoardCell.EMPTY;
					playerScore++;
					//represents left cell
				}
				if(!(above < 0)) {
					if(userSelected == board[above][left]) {
						board[above][left] = BoardCell.EMPTY;
						playerScore++;
						//represents upper left diagonal 
					}
				}
				if(!(below > getRows()-1)) {
					if(userSelected == board[below][left]) {
						board[below][left] = BoardCell.EMPTY;
						playerScore++;
						//represents lower left diagonal
					}
				}
			}
			if(!(right > getCols()-1)){
				if(userSelected == board[rowIndex][right]) {
					board[rowIndex][right] = BoardCell.EMPTY;
					playerScore++;
					//represents right cell
				}
				if(!(above < 0)) {
					if(userSelected == board[above][right]) {
						board[above][right] = BoardCell.EMPTY;
						playerScore++;
						//represents upper right diagonal
					}
				}
				if(!(below > getRows()-1)) {
					if(userSelected == board[below][right]) {
						board[below][right] = BoardCell.EMPTY;
						playerScore++;
						//represents lower right diagonal
					}
				}
			}
			if(!(above < 0)) {
				if(userSelected == board[above][colIndex]) {
					board[above][colIndex] = BoardCell.EMPTY;
					playerScore++;
					//represents above cell
				}
			}
			if(!(below > getRows()-1)) {
				if(userSelected == board[below][colIndex]) {
					board[below][colIndex] = BoardCell.EMPTY;
					playerScore++;
					//represents below cell;
				}
			}
		}
		
		collapse(board); //collapse the board by shifiting every row after an empty one, up
		
		
	}
	
	//Collapse the game board when a row is empty
	private void collapse(BoardCell [][] board) {
		int position = 0; //the current row on the board, works as a marker for an empty row
		
		for(int row = 0; row < board.length; row++) { 
			//goes through the board looking for an empty one
			if(rowIsEmpty(board[row])) { 
				position = row;
				/* goes through every row, starting from the current one, and makes it
				  the same as the one below */
				for(int next = position; next < board.length-1; next++) {
					board[next] = board[next+1];
					board[next+1] = emptyRow(getCols()); 
					//makes the lower row empty to continue the game
				}
			}
		}
	}
	
	//Creates an empty row
	private BoardCell[] emptyRow (int cols) {
		BoardCell [] empty = new BoardCell[cols]; //the empty row
		for(int col = 0; col < cols; col++) {
			empty[col] = BoardCell.EMPTY;
		}
		return empty;
	}
	
	//Tells you whether the board is empty or not
	private boolean rowIsEmpty(BoardCell[] row) {
		for(BoardCell cell : row) {
			//goes through the parameter seeing if the cell is empty
			if(cell != BoardCell.EMPTY) {
				return false;
			}
		}
		return true;
	}

}