package sudoku;
import java.io.IOException;

import sudoku.Board;

public class BoardTest {

	public static void main(String[] args) throws IOException {
		int topSpecLevel = 0;
		String allSolved = "All solved";
		for (int i=1 ; i<51 ; i++) {
			System.out.print("\n\nSolving grid number "+i+" :\n-----------------------------\n");
			Game game = new Game(i);
			
			System.out.print("Iitially, grid number "+i+" is as follows : \n");
			game.initialBoard.displayBoard(game.initialBoard.boardContent);
			Board solvedBoard = game.solveBoard3(game.initialBoard);
			solvedBoard.displayBoard(solvedBoard.boardContent);
			if (solvedBoard.checkBoard() && (solvedBoard.countBlanks()==0)) {
				System.out.println("Grid n°"+i+" is solved in "+game.totalNumberOfIterations+" iterations, reaching level "+game.maxLevelOfSpeculations+" of speculation.");
			}
			else {
				System.out.println("Error for grid n°"+i);
				allSolved = "At least one error";
			}
			if(game.maxLevelOfSpeculations>topSpecLevel) topSpecLevel = game.maxLevelOfSpeculations;
		}
		System.out.println(allSolved+" with a top level of speculation reaching "+topSpecLevel);
	}

}
