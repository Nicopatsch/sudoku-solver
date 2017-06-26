package sudoku;

import java.util.ArrayList;

public class Cell {
	int row;
	int column;
	int square;
	int value;
	ArrayList<Integer> possibilities = new ArrayList<Integer>();
	
	Cell(Cell otherCell) {
		this.row = otherCell.row;
		this.column = otherCell.column;
		this.square = otherCell.square;
		this.value = otherCell.value;
		this.possibilities = new ArrayList<Integer>(otherCell.possibilities);
	}
	
	Cell(int row, int column, int value) {
		this.value = value;
		this.row = row;
		this.column = column;
		this.square = (int)(column/3) + (int)(row/3)*3; //Index of the square calculated with the indexes of the cell
	}
	
	public boolean addPossibleValue(int value) {
		if(possibilities.indexOf(value)==-1) {
			possibilities.add(value);
			return true;
		}
		else return false;
	}
	
	public boolean removePossibleValue(int value) {
		if(possibilities.indexOf(value)>-1) {
			possibilities.remove(possibilities.indexOf(value));
			return true;
		}
		else return false;
	}
	
}
