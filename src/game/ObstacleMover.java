package src.game;

import src.environment.Board;
import src.environment.BoardPosition;
import src.environment.Cell;

public class ObstacleMover extends Thread {
	private Obstacle obstacle ;
	private Board board ;

	public ObstacleMover(Obstacle obstacle, Board board) {
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		BoardPosition boardPosition = new BoardPosition((int) (Math.random() * Board.WIDTH),
				(int) (Math.random() * Board.HEIGHT));
		boolean hasMoved;
		while(obstacle.getRemainingMoves() > 0) {
			hasMoved = false;
			if (!board.getCell(boardPosition).isOcupied()) {
				for (Cell[] cell : board.getCells()) {
					for (int j = 0; j < board.getCells()[0].length; j++) {
						if (cell[j].isOcupiedByObstacle()) {
							if (cell[j].getGameElement().equals(obstacle)) {
								cell[j].removeObstacle();
								cell[j].release();
								board.getCell(boardPosition).setGameElement(obstacle);
								board.setChanged();
								obstacle.decrementRemainingMoves();
								try {
									sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
									boardPosition = new BoardPosition((int) (Math.random() * Board.WIDTH), (int) (Math.random() * Board.HEIGHT));
									hasMoved = true;
									break;
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}
							}
						}
					}
					if(hasMoved) break;
				}
			} else {
				boardPosition = new BoardPosition((int) (Math.random() * Board.WIDTH), (int) (Math.random() * Board.HEIGHT));
			}
		}
	}
}
