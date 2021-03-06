package controllers.net;

import java.io.IOException;
import java.net.Socket;

import models.DomError;
import models.Movement;
import models.Piece;
import net.DominoLayer;

public class Communication extends DominoLayer {

	private String socket_desc;

	public Communication(Socket socket) throws IOException {
		super(socket);
		this.socket_desc = socket.getInetAddress() + ":" + socket.getPort();
	}

	/**
	 * Returns true if the first client message is equal to HELLO
	 * 
	 * @return
	 */
	public boolean waitClientHandshake() {

		while (true) {
			Id id = readHeader();

			if (id == Id.UNKNOWN) {
				return false;
			} else if (id == Id.TIMEOUT) {
				boolean socketAlive = this.socketAlive();
				if (!socketAlive) {
					return false;
				}
			} else if (id == Id.HELLO) {
				return true;
			}
		}

	}

	// SERVER SEE FROM CLIENT FUNCTIONS

	/**
	 * Returns the client movement
	 * 
	 * @return
	 */
	public Movement seeClientMovement() {
		char[] receivedChars = this.recieveChars(Size.MOVEMENT.asInt());
		return new Movement(receivedChars);

	}

	/**
	 * Returns the client chips quantity
	 * 
	 * @return
	 */
	public int seeClientHandLength() {
		return this.readInt();
	}

	// SERVER SEND FUNCTIONS

	/**
	 * Sends the initial movement to Client
	 * 
	 * @param clientPieces
	 * @param serverMovement
	 */
	public void sendInitMovement(Piece[] clientPieces, Movement serverMovement) {

		char[] initMovement = new char[Size.INITSERVER.asInt()];

		int counter = 0;
		// Construimos la parte de piezas del cliente
		for (Piece p : clientPieces) {
			initMovement[counter++] = p.getLeft();
			initMovement[counter++] = p.getRight();
		}
		// construimos la parte de piezas del movimiento del server
		for (char serverCharMovement : translateMovement(serverMovement, true)) {
			initMovement[counter++] = serverCharMovement;
		}

		if (sendHeader(Id.INITSERVER)) {
			sendChar(initMovement);
		}

	}

	/**
	 * Sends a movement to Client
	 * 
	 * @param serverMovement
	 * @param hand
	 * @param remaining
	 */
	public void sendServerMovement(Movement serverMovement, int hand,
			int remaining) {
		char[] chars = translateMovement(serverMovement);
		if (sendHeader(Id.MOVESERVER)) {
			sendChar(chars);
			sendInt(hand);
			sendInt(remaining);

		}
	}

	/**
	 * Sends a Piece to Client
	 * 
	 * @param piece
	 */
	public void sendPieceToClient(Piece piece) {
		char[] chars = translatePiece(piece);

		if (sendHeader(Id.MOVESERVER)) {
			sendChar(chars);
		}
	}

	/**
	 * Sends an Error to Client
	 * 
	 * @param err
	 */
	public void sendErrorToClient(DomError err) {
		this.sendError(err);
	}

	/**
	 * Sends Endgame to Client
	 * 
	 * @param clientHand
	 * @param serverHand
	 */
	public void sendEndGameToClient(int clientHand, int serverHand) {
		if (sendHeader(Id.ENDGAME)) {
			sendInt(clientHand);
			sendInt(serverHand);
		}
	}

	/**
	 * Gets the socket description
	 * 
	 * @return
	 */
	public String getScocketDescription() {
		return this.socket_desc;
	}

}
