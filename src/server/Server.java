package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import src.environment.BoardPosition;
import src.environment.Cell;
import src.environment.LocalBoard;
import src.remote.ActionResult;

public class Server {
	public static final int SERVER_PORT = 8080;
	private LocalBoard board;
	
	public void startServing(LocalBoard board) throws IOException {
		this.board = board;
		ServerSocket ss = new ServerSocket(SERVER_PORT);
		try {
			while(true){
				Socket socket = ss.accept();
				new DealWithClient(socket).start();
			}			
		} finally {
			ss.close();
		}
	}

	public LocalBoard getBoard() {
		return board;
	}
	
	public class DealWithClient extends Thread {

		private BufferedReader in;
		private ObjectOutputStream out;
		
		public DealWithClient(Socket socket) throws IOException {
			doConnections(socket);
		}
		
		@Override
		public void run() {
			try {
				serve();
			} catch (IOException e) {e.printStackTrace();}
		}

		void doConnections(Socket socket) throws IOException {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		
		private void serve() throws IOException {
			while (true) {
				boolean wasSuccessful = false;
				String[] str = in.readLine().split(";");
				Cell cell = board.getCell(new BoardPosition(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
				if (board.isFinished()) {
					out.writeObject(new ActionResult(wasSuccessful, true));
					break;
				}else if(cell.isOcupiedByObstacle()) {
					cell.removeObstacle();
					cell.release();
					wasSuccessful = true;
					out.writeObject(new ActionResult(wasSuccessful, false));
				}else if(cell.isOcupiedBySnake()) {
					if(cell.getOcuppyingSnake().wasKilled()) {
						for(Cell c : cell.getOcuppyingSnake().getCells()) {
							c.removeSnake();
							c.release();
						}
						wasSuccessful = true;
						out.writeObject(new ActionResult(wasSuccessful, false));
					}
				}else {
					out.writeObject(new ActionResult(wasSuccessful, false));
				}
			}
		}
		
	}
	
}
