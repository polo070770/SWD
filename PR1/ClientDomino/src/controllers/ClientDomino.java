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
		ENDGAME, PLAYING;
	}

	public enum Action {
		INIT, WAITNEXT, READERROR, READMOVE, SENDENDGAME, SENDMOVE, SENDNT, ENDGAME;
	}

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
			switch (ACTION) {

			case WAITNEXT:
				// recibimos la contestacion del servidor
				System.out.println("Esperando respuesta del servidor...");
				Id id = this.comm.readHeader();
				
				System.out.println("Recibo id-> " + id.name() + ":"
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

					// si el cliente no puede tirar pasamos al estado enviar NT
					ACTION = Action.SENDNT;

				} else {

					// enviamos movimiento al servidor
					sendMovement(currentClientMove);

					// aniadimos pieza en el tablero
					this.playedPile.pushSide(currentClientMove.getPiece(),
							currentClientMove.getSide());

					System.out.println("- Estado tablero: "
							+ playedPile.getRepresentation());

					if (this.player.handLength() == 0) {
						// Cliente gana juego
						ACTION = Action.ENDGAME;
					} else {
						// Continuamos jugando
						STATE = State.PLAYING;
						ACTION = Action.WAITNEXT;
					}

				}

				break;

			case SENDNT:

				// enviamos jugada con los datos necesarios
				sendMovement(currentClientMove);

				// esperamos respuesta del servidor
				ACTION = Action.WAITNEXT;

				break;

			case READERROR:
				DomError error = this.comm.seeError();
				if (error.getErrNum() == DomError.Id.ILLEGALACTION.asInt()) {
					Piece piece = currentClientMove.getPiece();
					System.out.println("la tengo? "
							+ this.player.hasPiece(piece));
					// si la tirada anterior era una pieza valida, y ya no esta
					// en la mano del jugador, la insertamos
					if ((!this.player.hasPiece(piece))
							&& this.getCatalog().hasPiece(piece)) {
						this.player.addPieceHand(piece);
					}
					System.out.println("Error rebut!!\n" + error.getDesc()
							+ "\n");
					System.out.println("- Estado tablero: "
							+ playedPile.getRepresentation());
					// el cliente tiene la oportunidad de jugar
					ACTION = Action.SENDMOVE;
				} else {

					System.out.println("Error rebut!!\n" + error.getDesc()
							+ "\n");
					System.out
							.println("Desconectant del servidor, torna a comen�ar!");
					closeGame();
				}
				break;

			case SENDENDGAME:
				// TODO
				break;

			case ENDGAME:
				System.out
						.println("La partida finalitza amb la seguent puntuacio:\n"
								+ this.comm.seeScore());
				closeGame();

				break;

			}
		}

	}

	/**
	 * Funcion que devuelve una accion a realizar a partir de una id
	 * 
	 * @param id
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

		case MOVESERVER: // el servidor hace move
			return Action.READMOVE;

		case UNKNOWN: // id con cabecera desconocida, esperamos a cabecera
						// valida
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
		if (move.getPiece() != null)
			this.player.removePiece(move.getPiece());
		// enviamos la jugada con los datos necesarios
		this.comm.sendClientMovement(move, this.player.handLength());
	}

	/**
	 * Funcion que cierra la conexion con el servidor y hace terminar el juego
	 */
	private void closeGame() {
		this.STATE = State.ENDGAME;
		this.comm.closeConnection();

	}
}