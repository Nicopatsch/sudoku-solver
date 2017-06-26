package sudoku;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
	
	Board initialBoard;
	Board solvingBoard;
	int levelOfSpeculations;
	int maxLevelOfSpeculations;
	int totalNumberOfIterations;
	boolean solved;
	
	public Game(int gridNumber) throws IOException {
		initialBoard = new Board(gridNumber);
		solvingBoard = new Board(gridNumber);
		this.levelOfSpeculations = 0;
		this.maxLevelOfSpeculations = 0;
		this.totalNumberOfIterations = 0;
		this.solved = false;
		
	}
	
	
	public Board solveBoard3(Board board) throws IOException{
		this.totalNumberOfIterations++;
		
		
		board.classicalSolving();
		if(board.countBlanks()==0 && board.checkBoard()) return board; //La grille est résolue
		
		//Si aucune possibilité,
		else if(!board.checkBoard()) return new Board(); //Empty board
		
		else {
			Board specBoard = new Board(board);
			int[] coo = specBoard.searchCellToSpeculate(0, 0);
			int value = specBoard.boardContent[coo[0]][coo[1]].possibilities.get(0);
			specBoard.boardContent[coo[0]][coo[1]].value = value;
			specBoard.classicalSolving();
			
			//La grille est bonne, on la retourne
			Board temp = solveBoard3(specBoard);
			if(temp.checkBoard()) return temp;
			
			else { //La grille est mauvaise, on respécule
				board.boardContent[coo[0]][coo[1]].removePossibleValue(value);
				if(board.boardContent[coo[0]][coo[1]].possibilities.size()>0) {
					this.updateSpeculationCount();
					return solveBoard3(board);
				}
				else {
					this.levelOfSpeculations--;
					return solveBoard3(new Board());
				}
			}
		}
	}
	
	
	private void updateSpeculationCount() {
		this.levelOfSpeculations++;
		if (this.maxLevelOfSpeculations<this.levelOfSpeculations) {
			this.maxLevelOfSpeculations = this.levelOfSpeculations;
		}
	}

}
