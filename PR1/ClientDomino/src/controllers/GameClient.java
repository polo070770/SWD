package controllers;

import java.net.Socket;

import controllers.net.Communication;

public class GameClient {

	private Communication comm;

	public GameClient(Socket socket) {

		try {
			this.comm = new Communication(socket);

			// this.comm.readId(); // para probar timeouts

			// Enviamos el primer mensaje, para que el servidor entienda que
			// hablamos el mismo idioma
			boolean sincronized = this.comm.requestHandShake();

			if (sincronized) {
				System.out.println("Connected to server!");
				// iniciamos juego
				ClientDomino game = new ClientDomino(comm);
				game.initGame();

			} else {
				System.out.println("No handhake, closing connection");
				if (this.comm.closeConnection()) {
					System.out.println("Connection closed");
					System.out.println("BYE");
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
}
