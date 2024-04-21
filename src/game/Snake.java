package src.game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import src.environment.LocalBoard;
import src.gui.SnakeGui;
import src.environment.Board;
import src.environment.BoardPosition;
import src.environment.Cell;

public abstract class Snake extends Thread {

	private boolean killed = false ;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private Board board;

	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
		doInitialPositioning();
	}


	public void killSnake () { killed = true ; }
	public boolean wasKilled () { return killed == true ;}

	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getCurrentLength() {
		return cells.size();
	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

	protected void move(Cell cell) throws InterruptedException {
		cell.request(this);
		cells.addLast(cell);
		if(cells.size() > size) {
			cells.removeFirst().release();
		}
	}

	// The snake's initial position is randomly attributed to a row in the 1st colum specifically
	// The selected position is then added as to the list of cells only if it can successfully request for
	// the cell, which means the cell must be empty or else the method will define a new position randomly
	// for the snake and then repeat the process until the snake obtains a position
	protected void doInitialPositioning()  {
		BoardPosition boardPosition = new BoardPosition( 0, (int) (Math.random()*Board.HEIGHT));
		while(true) {
			if (!board.getCell(boardPosition).isOcupied()) {
				try {
					board.getCell(boardPosition).request(this);
					cells.add(board.getCell(boardPosition));
					break;
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			} else {
				boardPosition = new BoardPosition( 0 , (int) (Math.random()*Board.HEIGHT));
			}
		}
	}

	public Board getBoard() {
		return board;
	}

	// Utility method to return cells occupied by snake as a list of BoardPosition
	// Used in GUI. Do not alter
	public synchronized LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}

		return coordinates;
	}
	
	public List<BoardPosition> getPossibleMovementPositions(){
		List<BoardPosition> possiblePositions = board.getNeighboringPositions(cells.getLast());
		possiblePositions.removeIf(bp -> {
			for(Cell c : this.cells){
				if(c.getPosition().equals(board.getCell(bp).getPosition())) {
					return true;
				}
			}
			return false;
		});
		return possiblePositions;
	}
	
	public synchronized void consumeGoal(Goal goal) {
		size += goal.getGoalValue();
		if(goal.getGoalValue() < Goal.MAX_VALUE) {
			try {
				goal.incrementValue();
				board.addGameElement(goal);
			} catch (InterruptedException e) {e.printStackTrace();}
		} else {
			board.finishGame();
		}
	}
}
