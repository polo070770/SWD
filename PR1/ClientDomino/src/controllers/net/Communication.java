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

	public char[] readNextMovementChar() {

		Id recievedId = readHeader();

		if (recievedId == Id.MOVE) {

			char[] receivedChars;
			receivedChars = this.recieveChars(Size.MOVEMENT.asInt());

			while (receivedChars.length == 0 && this.socketAlive()) {
				receivedChars = this.recieveChars(Size.INIT.asInt());
			}

			return receivedChars;

		}

		System.out.println("Retornant error, identificador rebut"
				+ recievedId.getVal() + "no es igual a MOVE: "
				+ Id.MOVE.getVal());

		return new char[0];

	}

	public char[] readMovementChar() {

		char[] receivedChars;
		receivedChars = this.recieveChars(Size.MOVEMENT.asInt());

		return receivedChars;

	}

	public void sendClientMovement(Movement serverMovement, int hand) {
		char[] chars = translateMovement(serverMovement);
		if (sendHeader(Id.MOVE)) {
			sendChar(chars);
			sendInt(hand);
		}
	}
	
	public void sendClientNTMovement(Movement serverMovement, int hand) {
		char[] chars = translateMovement(serverMovement);
		if (sendHeader(Id.MOVESERVER)) {
			sendChar(chars);
			sendInt(hand);
		}
	}

	public Movement seeServerMovement() {
		char[] receivedChars = this.recieveChars(Size.MOVEMENT.asInt());
		return new Movement(receivedChars);
	}

}