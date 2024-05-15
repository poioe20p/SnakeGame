package src.remote;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import src.environment.Board;
import src.environment.BoardPosition;
import src.environment.LocalBoard;
import src.gui.SnakeGui;
import src.server.Server;

public class Client {
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;

	public static void main(String[] args) {
		// TODO
		new Client().runClient();
	}

	public void runClient() {
		try {
			connectToServer();
			sendMessages();
		} catch (IOException | ClassNotFoundException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {

			}
		}
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		System.out.println("Endereco:" + endereco);
		socket = new Socket(endereco, Server.SERVER_PORT);
		System.out.println("Socket:" + socket);
		in =  new ObjectInputStream(socket.getInputStream());
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	void sendMessages() throws IOException, ClassNotFoundException {
		String x = String.valueOf((int) (Math.random()*Board.WIDTH));
		String y = String.valueOf((int) (Math.random()*Board.HEIGHT));
		out.println(x + ";" + y);
		ActionResult actionResult = (ActionResult) in.readObject();
		while(!actionResult.gameEnded()) {
			if(actionResult.wasSuccessful()) {
			}

			try {
				Thread.sleep(1);
				x = String.valueOf((int) (Math.random()*Board.WIDTH));
				y = String.valueOf((int) (Math.random()*Board.HEIGHT));
				out.println(x + ";" + y);
				actionResult = (ActionResult) in.readObject();
			}catch (InterruptedException e) {
			}
		}
	}


}
