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
}
