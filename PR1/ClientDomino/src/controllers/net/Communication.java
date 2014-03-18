package controllers.net;

import java.io.IOException;
import java.net.Socket;

import models.Movement;
import net.DominoLayer;

public class Communication extends DominoLayer {

	public Communication(Socket socket) throws IOException {
		super(socket);
	}

	/**
	 * Returns true if the first client message is equal to HELLO
	 * 
	 * @return
	 */
	public boolean requestHandShake() {
		// el mensaje de handhake o sincronizacion es un Hello
		return writeId(Id.HELLO);
	}

	/**
	 * Returns the player chips and who starts the game
	 * 
	 * @return
	 */
	public char[] readInitMovementChar() {

		Id recievedId = readHeader();

		if (recievedId == Id.INIT) {
			char[] receivedChars;
			receivedChars = this.recieveChars(Size.INIT.asInt());

			while (receivedChars.length == 0 && this.socketAlive()) {
				receivedChars = this.recieveChars(Size.INIT.asInt());
			}

			return receivedChars;
		}

		System.out.println("Retornant error, identificador rebut"
				+ recievedId.getVal() + "no es igual a INIT: "
				+ Id.INIT.getVal());

		return new char[0];
	}

	/**
	 * Returns the server movement
	 * 
	 * @return
	 */
	public Movement seeServerMovement() {
		char[] receivedChars = this.recieveChars(Size.MOVEMENT.asInt());
		return new Movement(receivedChars);
	}

	public String seeScore() {
		int scoreClient = this.readInt();
		int scoreServer = this.readInt();
		return ("Client Score: " + scoreClient + "\nServer Score: " + scoreServer);
	}

	/**
	 * Sends a user movement, it can be: " i cant trow" too.
	 * 
	 * @param clientMovement
	 * @param hand
	 *            , number of user chips
	 */
	public void sendClientMovement(Movement clientMovement, int hand) {
		char[] chars = translateMovement(clientMovement);
		if (sendHeader(Id.MOVE)) {
			sendChar(chars);
			sendInt(hand);
		}
	}

}