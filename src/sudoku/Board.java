package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Board {
	public static final int DIMENSION = 9;
	public static final int SQUARES = 9;
	public static final int SQUARE_DIM = 3;
	Cell[][] boardContent = new Cell [DIMENSION][DIMENSION];
	Cell[][] boardSquares = new Cell [DIMENSION][DIMENSION];
	int[][][] boardPossibilities = new int[DIMENSION][DIMENSION][DIMENSION];
	int gridNumber;
	
	
	public Board(int i) throws IOException {
		this.gridNumber=i;
		this.fillBoard();
		this.boardSquares = createSquares();
		this.searchPossibilities();
		
	}
	
	public Board(Board otherBoard) {
		for(int i=0 ; i<DIMENSION ; i++) {
			for(int j=0 ; j<DIMENSION ; j++){
				this.boardContent[i][j] = new Cell(otherBoard.boardContent[i][j]);
				this.boardSquares[i][j] = new Cell(otherBoard.boardSquares[i][j]);
			    System.arraycopy( otherBoard.boardPossibilities[i][j], 0, this.boardPossibilities[i][j], 0, DIMENSION);
			}
		}
	  }
	
	public Board() { //Constructeur d'une grille vide
		this.gridNumber=0;
		this.boardContent = null;
	}
	
	//Method that creates the 2D array contentSquares = Each line of this array contains the content of  square
	private Cell[][] createSquares() { 
		Cell[][] squares = new Cell[DIMENSION][DIMENSION];
		for (int k=0 ; k<SQUARE_DIM ; k++) {
			for(int n=0 ; n<SQUARE_DIM ; n++) {
				Cell[] tmp = new Cell[DIMENSION];
				for(int row=0 ; row<3 ; row++) {
					for(int column=0 ; column<3 ; column++) {
						tmp[3*row+column] = new Cell(row, column, boardContent[row+3*k][column+3*n].value);
					}
				}
				System.arraycopy( tmp, 0, squares[3*k+n], 0, DIMENSION );
			}
		}
        return squares;   
	}
	
	public void displayBoard(Cell[][] myArray) {
		for (int i = 0; i < myArray.length; i++) {
	        if (i > 0) System.out.println();
	        System.out.print(" --- --- --- --- --- --- --- --- ---\n| ");
	        for (int j = 0; j < myArray.length; j++) {
	            if (j > 0) System.out.print(" | ");
	        System.out.print(myArray[i][j].value);
	        }
	        System.out.print(" |");
		}
		System.out.print("\n --- --- --- --- --- --- --- --- ---\n");
	}
	
	private void fillBoard() throws IOException {
		
		if(this.gridNumber == 0) {
			for (int row=0 ; row<9 ; row++) {
		    	for (int column=0 ; column<DIMENSION ; column++) {
		    		boardContent[row][column] = new Cell(row, column, 0);
		    	}
		    }
		} else {
			BufferedReader br = new BufferedReader(new FileReader("./src/sudoku/sudoku.txt"));
			try {
			    String line = br.readLine();
			    
			    /*Jumping to the declaration of the gread we are looking for*/
			    String flagLine = "Grid ";
			    if (this.gridNumber<=9) flagLine += "0";
			    flagLine += this.gridNumber;
			    while (line!=null && !line.equals(flagLine)) line = br.readLine();
			    line = br.readLine();
			    
			    /*Reading through the board*/
			    for (int row=0 ; row<9 ; row++) {
			    	for (int column=0 ; column<DIMENSION ; column++) {
			    		boardContent[row][column] = new Cell(row, column, Character.getNumericValue(line.charAt(column)));
			    	}
			        line = br.readLine();
			    }
			} finally {
			    br.close();
			}
		}
	}
	
	//checkBoard() checks whether the board contains mistakes --> returns true if everything is ok
	public boolean checkBoard() { 
		if(this.boardContent == null) return false;
		boolean valid = true;
		for (int i=0 ; i<DIMENSION ; i++) {
			for (int j=0 ; j<DIMENSION ; j++) {
				if (numberOfOccurrences(j+1, i, -1, -1)>=2 
				|| numberOfOccurrences(j+1, -1, i, -1)>=2 
				|| numberOfOccurrences(j+1, -1, -1, i)>=2) {
					valid = false;
				}
				else if(this.boardContent[i][j].possibilities.size()==0 && this.boardContent[i][j].value==0){
					valid = false;
				}
			}
		}
		return valid;
	}
	
	
	//If we are considering a row, set column and square to -1 and row to its index
	//If we are considering a column, row and square to -1 and column to its index 
	//If we are considering a square, row and column to -1 and square to its index
	private int numberOfOccurrences(int value, int row, int column, int square) {
		int nb = 0;
		for (int i=0 ; i<DIMENSION ; i++) {
			if (row>-1 && boardContent[row][i].value==value) nb++; //Considering a row;
			if (column>-1 && boardContent[i][column].value==value) nb++; //Considering a column
			if (square>-1 && boardSquares[square][i].value==value) nb++; //Considering a square
		}
		return nb;
	}
	
	//Checks whether the argument value is already present in the current line, column and square
	public boolean valueIsPresent(int value, Cell cell) {
		boolean present = false;
		for(int i=0 ; i<DIMENSION ; i++) {
	        if (this.boardContent[i][cell.column].value==value) present = true;
	        else if (this.boardContent[cell.row][i].value==value) present = true;
	        else if (this.boardSquares[cell.square][i].value==value) present = true;
		}
		return present;
	}
	
	public void searchPossibilities() {
		for (int row=0 ; row<DIMENSION ; row++) {
			for (int column=0 ; column<DIMENSION ; column++) {
				for (int value=1 ; value<=DIMENSION ; value++) {
					if (valueIsPresent(value, boardContent[row][column])) {
						this.boardContent[row][column].removePossibleValue(value);
					}
					else {
						this.boardContent[row][column].addPossibleValue(value);
					}
				}
			}
		}
	}
	
	public int countBlanks() {
		int count = 0;
		for(int row=0 ; row<DIMENSION ; row++) {
			for (int column=0 ; column<DIMENSION ; column++) {
				if (boardContent[row][column].value==0) count++; } }
		return count;
	}
	
	
	//checks the whole board for blank case to fill and returns the number of blanks filled
	public int fillBlanks() {
		int filledCells = 0;
		for (int row=0 ; row<DIMENSION ; row++) {
			for (int column=0 ; column<DIMENSION ; column++) {
				if(this.boardContent[row][column].value==0) {
					if (boardContent[row][column].possibilities.size() == 1) {
						boardContent[row][column].value = boardContent[row][column].possibilities.get(0);
						filledCells++;
					}
				}
			}
		}
		return filledCells;
	}
	
	
	public void classicalSolving() {
		int numberOfChanges = 1;
		while(numberOfChanges > 0) {
			numberOfChanges = this.fillBlanks();
			this.searchPossibilities();
		}
	}
	
	public int[] searchCellToSpeculate (int firstRow, int firstColumn) {
		int minRow = firstRow;
		int minColumn = firstColumn;
		int minPossibilities = 9; //Nombre minimum de possibilités
		int row = firstRow;
		int column = firstColumn+1;
		
		while(row<DIMENSION) {
			while (column<DIMENSION) {
//				System.out.print("Number of possibilities for cell in ( " + row + " ; " + column + " ) = " + this.boardContent[row][column].possibilities.size() + "\n");
				if (this.boardContent[row][column].possibilities.size() < minPossibilities && this.boardContent[row][column].value == 0 && this.boardContent[row][column].possibilities.size()!=0) {
					//System.out.print("Changing minRow, minColumn from ( "+minRow+" ; "+minColumn+" ) to ( "+row+" ; "+column+" ) in searchCellToSpeculate()\n");
					minRow = row;
					minColumn = column;
					minPossibilities = this.boardContent[minRow][minColumn].possibilities.size();
				}
				column++;
			}
			column = 0;
			row++;
		}
		int[] coordinates = new int[2];
		coordinates[0]=minRow;
		coordinates[1]=minColumn;
		if (minPossibilities == 0) {
			System.out.print("\n\n------------------\n------ERREUR------\n-----------------\n\n");
		}
		return coordinates;
	}
	
	
}