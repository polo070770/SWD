package main;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import controllers.GameClient;

public class MainClient {

	private String url = "127.0.0.1";
	private int port = 1234;
	InetAddress host;

	/**
	 * @param args
	 *            |0
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainClient mainClient = new MainClient(args);

	}

	public MainClient(String[] args) {
		Socket socket;

		if (args.length > 2) {
			// capturamos la url
			url = args[1].split(":")[0];
			// capturamos el puerto
			port = Integer.parseInt(args[1].split(":")[1]);
		}

		try {

			host = InetAddress.getByName(url);
			socket = new Socket(host, port);
			socket.setKeepAlive(true);

			new GameClient(socket);

		} catch (ConnectException e) {
			System.out.println("No hem pogut conectar al servidor");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("No hem pogut conectar al servidor");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("No hem pogut conectar al servidor");
			e.printStackTrace();
		}

	}

}
