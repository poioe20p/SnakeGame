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
		while(!wasKilled() && !getBoard().isFinished()) {
			try {
				List<BoardPosition> possibleMovementPositions = getPossibleMovementPositions();
				if(!possibleMovementPositions.isEmpty()) {
					move(getBoard().getCell(getBoard().selectPositionClosestToGoal(possibleMovementPositions)));
					getBoard().setChanged();
					sleep(Board.PLAYER_PLAY_INTERVAL);
				} else {
					this.killSnake();
				}
			} catch (InterruptedException e) {
				try {
					sleep(Board.PLAYER_PLAY_INTERVAL);
				} catch (InterruptedException e1) {}
			}
		}
	}
	

	
}
