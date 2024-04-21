package src.environment;


import src.game.GameElement;
import src.game.Goal;
import src.game.Killer;
import src.game.Obstacle;
import src.game.Snake;

import java.util.LinkedList;

import src.game.AutomaticSnake;

public class Cell{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement = null;

	public GameElement getGameElement() {
		return gameElement;
	}


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}

	// request a cell to be occupied by Snake, If it is occupied by another Snake or Obstacle, wait (snake doesn't move).
	public synchronized void request(Snake snake) throws InterruptedException {
		while(isOcupied() && !isOcupiedByGoal()) {
			if(!snake.getBoard().selectPositionClosestToGoal(snake.getPossibleMovementPositions()).equals(position)) {
				snake.interrupt();
			} else if(isOccupiedByKiller()) {
				snake.killSnake();
				snake.interrupt();
			}
			wait();
		}
		if (isOcupiedByGoal()) {
			removeGoal().captureGoal(snake);;
			for (Snake s : snake.getBoard().getSnakes()) {
				 s.interrupt();
			}
		}
		ocuppyingSnake = snake;
	}

	public synchronized void release() {
		ocuppyingSnake = null;
		gameElement = null;
		notifyAll();
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}

	@Override
	public String toString() {
		return "" + position;
	}

	public void setGameElement(GameElement obstacle) {
		gameElement = obstacle;
	}

	// Returns true if there's a non-null instance of occupyingSnake or gameElement
	public boolean isOcupied() {
		return ocuppyingSnake != null || gameElement != null;
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() {
		Goal goal = (Goal) gameElement;
		gameElement = null;
		return goal;
	}

	public void removeObstacle() {
		gameElement = null;
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}


	public boolean isOccupiedByKiller() {
		return (gameElement!=null && gameElement instanceof Killer);
	}


	public boolean isOcupiedByObstacle() {
		return (gameElement!=null && gameElement instanceof Obstacle);
	}


	public void removeSnake(Snake snake) {
		//TODO
	}



}
