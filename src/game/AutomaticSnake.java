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
		while(!wasKilled() || !getBoard().isFinished()) {
			try {
				move(getBoard().getCell(getBoard().selectPositionClosestToGoal(getPossibleMovementPositions())));
				getBoard().setChanged();
				sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
				try {
					sleep(Board.PLAYER_PLAY_INTERVAL);
				} catch (InterruptedException e1) {}
			}
		}
	}
	

	
}
