package src.game;

import src.environment.Board;
import src.environment.LocalBoard;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=9;
	public Goal( Board board2) {
		this.board = board2;
	}
	public int getValue() {
		return value;
	}
	public void incrementValue() throws InterruptedException {
		value++;
	}

	public int getGoalValue() {
		return value;
	}
	
	public synchronized void captureGoal(Snake snake) {
		snake.addSize(this.getGoalValue());
		if(this.getGoalValue() < Goal.MAX_VALUE) {
			try {
				this.incrementValue();
				board.addGameElement(this);
			} catch (InterruptedException e) {e.printStackTrace();}
		} else {
			LocalBoard.pool.shutdownNow();
			board.finishGame();
		}
	}
}
