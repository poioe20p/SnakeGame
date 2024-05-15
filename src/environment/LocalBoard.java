package src.environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.game.*;
import src.server.Server;

public class LocalBoard extends Board{
	
	private static final int NUM_SNAKES = 6;
	private static final int NUM_OBSTACLES = 25;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	private static final int NUM_WAITERS = 3;
	public static ExecutorService pool;

	public LocalBoard() {
		// TODO
		// place game elements and snakes
		for(int i = 0; i < NUM_SNAKES; i++) {
			addSnake(new AutomaticSnake(i, this));
		}
		addGoal();
		addObstacles(NUM_OBSTACLES);
		setChanged();
	}

	// synchronization in cell
	
	public void init() {
		// TODO
		// Start Threads
		for(Snake snake : snakes) {
			snake.start();
		}
		pool = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		CyclicBarrier barrier = new CyclicBarrier(NUM_WAITERS, new Runnable() {
			@Override
			public void run() {
				addGameElement(new Killer());
				setChanged();
			}
		});
		for(Obstacle obstacle: getObstacles()) {
			pool.submit(new ObstacleMover(obstacle, this, barrier));
		}
	}

	
	
	public void removeSnake(BoardPosition position) {
//		TODO
	}



	// Ignore these methods: only for remote players, which are not present in this project
	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}



}
