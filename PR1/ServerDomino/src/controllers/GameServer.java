package controllers;

import java.net.Socket;

import controllers.net.Communication;

public class GameServer implements Runnable {

	private Communication comm;
	private Socket socket;
	private boolean connected;

	public GameServer(Socket socket) {
		this.socket = socket;
		try {
			this.comm = new Communication(socket);
			connected = true;
		} catch (Exception e) {
			connected = false;
			System.out.println(e.toString());
		}

	}

	public boolean isConnected() {
		return this.connected;
	}

	public void disconnect() {
		this.connected = false;
		comm.closeConnection();
	}

	@Override
	public void run() {
		if (connected) {
			// aqui hacemos la prueba de si el cliente habla nuestro protocolo,
			// sino
			// nada
			Boolean validHandShake = comm.waitClientHandshake();

			if (validHandShake) {
				System.out.println("Correct handshaking, starting Game");

				// una vez hemos recibido el mensaje inicial del cliente, y
				// siendo este valido, iniciamos el juego
				ServerDomino game = new ServerDomino(comm);
				game.initGame();
				System.out.println(socket.getInetAddress() + ":"
						+ socket.getPort() + " disconnected");
				disconnect();
			} else {
				System.out.println(socket.getInetAddress() + ":"
						+ socket.getPort() + " refused");
				disconnect();

			}

		}

	}

}
