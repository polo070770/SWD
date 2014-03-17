package controllers;

import models.DomError;
import models.Movement;
import models.Piece;
import models.Pile;
import models.Side;
import net.DominoLayer.Id;
import view.Human;
import controllers.net.Communication;

public class ClientDomino extends Domino {
	public enum State {
		CLIENTNP, SERVERNP, CLIENTMOVE, ERRORSUBMIT, ENDGAME, WAITING, PLAYING;
	}

	public enum Action {
		INIT, WAITNEXT, READERROR, SENDERROR, READMOVE, CLIENTMOVE, CLIENTERROR, SENDENDGAME, CLIENTNT, SENDPIECE, SENDMOVE, SERVERMOVE, SERVERNT, SENDNT, ENDGAME;
	}

	private Pile remainingPile;
	private Pile clientHand;

	private Communication comm;
	private Piece startingPiece;

	private State STATE;
	private Action ACTION;

	private Movement currentClientMove;
	private Movement currentServerMove;

	public ClientDomino(Communication comm) {
		super(); // create super resources
		this.comm = comm;
		STATE = State.PLAYING; // estamos en el estado de jugar
		ACTION = Action.INIT; // la accion que toca es la de inicio de partida
		createClientResources();
	}

	private void createClientResources() {
		Pile hand = new Pile();

		char[] resources = this.comm.readInitMovementChar();

		// creamos los recursos del cliente a partir de los datos recibidos
		int i;
		for (i = 0; i < this.HANDSIZE * 2; i += 2) {
			// fichas de inicio recibidas para el cliente
			hand.addPiece(new Piece(resources[i], resources[i + 1]));
		}

		this.player = new Human(hand, this.getCatalog());

		// 4 ultimos bytes que indican quien juega
		char[] movementChars = new char[4];
		movementChars[0] = resources[i++];
		movementChars[1] = resources[i++];
		movementChars[2] = resources[i++];
		movementChars[3] = resources[i++];

		currentServerMove = new Movement(movementChars);

		initGame();

	}

	public void initGame() {

		while (this.STATE != State.ENDGAME) {
			switch (this.ACTION) {

			case WAITNEXT:
				// recibimos la contestacion del servidor
				System.out.println("Esperando respuesta del servidor...");
				Id id = this.comm.readHeader();
				System.out.println("Recibo id: " + id.name() + ":"
						+ id.getVal());
				ACTION = convertIdToAction(id);
				break;

			case INIT:
				// analizamos la primera respuesta del servidor
				if (currentServerMove.isNT()) {
					// es un NT-> servidor no puede tirar, empieza cliente
					currentClientMove = this.player.getFirstMovement();

					System.out.println("- Ficha cliente: "
							+ currentClientMove.getRepresentation());

					// aniadimos al tablero la pieza jugada por el servidor
					this.playedPile.pushSide(currentClientMove.getPiece(),
							currentClientMove.getSide());
					System.out.println("- Estado tablero: "
							+ this.playedPile.getRepresentation());

					// enviamos movimiento al Server
					sendMovement(currentClientMove);

					// Esperamos respuesta del servidor
					ACTION = Action.WAITNEXT;

				} else {
					// el servidor ha empezado a jugar primero, toca jugar a
					// cliente
					System.out.println("- Ficha server: "
							+ currentServerMove.getRepresentation());

					this.startingPiece = currentServerMove.getPiece();
					this.playedPile.pushSide(this.startingPiece, Side.LEFT);

					System.out.println("- Estado tablero: "
							+ playedPile.getRepresentation());

					ACTION = Action.SENDMOVE;

				}

				break;
			case READMOVE:

				// leemos la respuesta del servior
				currentServerMove = this.comm.seeServerMovement();

				if (currentServerMove.isNT()) {

					STATE = State.SERVERNP;

					// si el servidor no puede tirar, el cliente tira
					if (this.player.handLength() > 0) {
						// tira si aun tiene fichas en la mano
						ACTION = Action.SENDMOVE;
					} else {
						// sino contestara con un no puedo tirar
						ACTION = Action.SENDNT;
					}

				} else if (currentServerMove.isNewPiece()) {
					// el servidor nos ha enviado una ficha nueva
					System.out.println("\n- Ficha recibida: "
							+ currentServerMove.getRepresentation());

					this.player.addPieceHand(currentServerMove.getPiece());

					System.out.println("- Estado tablero: "
							+ playedPile.getRepresentation());

					ACTION = Action.SENDMOVE;

				} else {
					// el servidor ha tirado una ficha, nos toca contestar
					System.out.println("\n- Ficha server: "
							+ currentServerMove.getRepresentation());

					System.out.println(currentServerMove.getPiece().reversed());

					// aniadimos ficha del servidor en el tablero
					this.playedPile.pushSide(currentServerMove.getPiece(),
							currentServerMove.getSide());

					System.out.println("- Estado tablero: "
							+ playedPile.getRepresentation());

					// Contestamos al servidor con un movimiento
					ACTION = Action.SENDMOVE;

				}
				break;

			case SENDMOVE:

				// construimos nuevo movimiento
				currentClientMove = this.player.nextMove(playedPile);

				// analizamos si el movimiento del cliente es un no puedo tirar
				if (this.currentClientMove.isNT()) {

					// si el cliente no puede tirar pasamos al esado enviar NT
					ACTION = Action.SENDNT;

				} else {

					System.out.println("#fichas: " + this.player.handLength());

					// enviamos movimiento al servidor
					sendMovement(currentClientMove);

					// aniadimos pieza en el tablero
					this.playedPile.pushSide(currentClientMove.getPiece(),
							currentClientMove.getSide());

					if (this.player.handLength() == 0) {
						// Cliente gana juego
						ACTION = Action.SENDENDGAME;
					} else {
						// Continuamos jugando
						STATE = State.PLAYING;
						ACTION = Action.WAITNEXT;
					}

				}
				break;
			case SENDNT:
				STATE = State.CLIENTNP;
				// enviamos jugada con los datos necesarios
				this.comm.sendClientMovement(currentClientMove,
						this.player.handLength());

				// esperamos respuesta del servidor
				ACTION = Action.WAITNEXT;
				break;

			case READERROR:
				DomError error = this.comm.seeError();
				break;

			case ENDGAME:
				closeGame();
				break;

			}
		}

	}

	/**
	 * @param id
	 *            Funcion que devuelve una accion a realizar a partir de una id
	 * @return
	 */
	private Action convertIdToAction(Id id) {
		switch (id) { // parseamos la id recibida con su accion

		case TIMEOUT:// si es timeout no pasa nada
			return Action.WAITNEXT;

		case ERROR: // si el servidor nos dice que es error esperamos
			return Action.READERROR;

		case ENDGAME: // la dominolayer nos puede indicar endgame debido a algun
						// error IO
			return Action.ENDGAME;

		case MOVESERVER:// el servidor hace move
			return Action.READMOVE;

		case UNKNOWN:
			return Action.WAITNEXT;

		default:
			return Action.SENDENDGAME;
		}
	}

	/**
	 * Funcion que elimina la ficha de la pila del servidor y la envia cliente
	 * 
	 * @param move
	 */
	private void sendMovement(Movement move) {
		// la eliminamos
		this.player.removePiece(move.getPiece());
		// enviamos la jugada con los datos necesarios
		this.comm.sendClientMovement(move, this.player.handLength());
	}

	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.ENDGAME;

	}
}