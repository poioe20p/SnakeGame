package src.game;

import src.environment.Board;
import src.environment.BoardPosition;
import src.environment.Cell;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ObstacleMover extends Thread {
	private Obstacle obstacle ;
	private Board board ;
	private CyclicBarrier barrier;

	public ObstacleMover(Obstacle obstacle, Board board, CyclicBarrier barrier) {
		this.obstacle = obstacle;
		this.board = board;
		this.barrier = barrier;
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
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}
}
