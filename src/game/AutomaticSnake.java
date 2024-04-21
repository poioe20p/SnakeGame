package src.game;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import src.environment.LocalBoard;
import src.gui.SnakeGui;
import src.environment.Cell;
import src.environment.Board;
import src.environment.BoardPosition;

public class AutomaticSnake extends Snake {
	
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}

	@Override
	public void run() {
		while(!wasKilled()) {
			List<BoardPosition> possiblePositions = getBoard().getNeighboringPositions(cells.getLast());
			possiblePositions.removeIf(bp -> {
				for(Cell c : this.cells){
					if(c.getPosition().equals(getBoard().getCell(bp).getPosition())) {
						return true;
					}
				}
				return false;
			});
			try {
				move(getBoard().getCell(getBoard().selectPositionClosestToGoal(possiblePositions)));
				getBoard().setChanged();
				sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	

	
}
