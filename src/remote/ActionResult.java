package src.remote;

public class ActionResult {
	
	private boolean	wasSuccessful;
	private boolean gameEnded;
	
	public ActionResult(boolean wasSuccessful, boolean gameEnded) {
		this.wasSuccessful = wasSuccessful;
		this.gameEnded = gameEnded;
	}
	
	public boolean wasSuccessful() {
		return wasSuccessful;
	}
	public boolean gameEnded() {
		return gameEnded;
	}
	
	
}
