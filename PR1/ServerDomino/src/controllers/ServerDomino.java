package controllers;

import models.Movement;
import models.Piece;
import models.Pile;
import net.DominoLayer.Id;
import controllers.net.Communication;

public class ServerDomino extends Domino {
	public enum State {
		CLIENTNP, SERVERNP, CLIENTMOVE, ERRORSUBMIT, ENDGAME, WAITING, PLAYING;
	}

	private Pile remainingPile;
	private Pile clientHand;
	private Communication comm;
	private Piece startingPiece;

	private State STATE = State.PLAYING;
	private Id ACTION = Id.INIT;

	public ServerDomino(Communication comm) {
		super(); // create super resources
		this.comm = comm;
		createDominoResources(); // creamos los recursos del juego

	}

	/**
	 * Function that creates the different resources for the domino game
	 */
	private void createDominoResources() {
		// creamos la pila de fichas pendientes a partir del catalogo
		this.remainingPile = new Pile(this.getCatalog());

		// creamos la mano del jugador
		Pile hand = new Pile(this.remainingPile.getAmount(HANDSIZE));

		// creamos el jugador y le asignamos su mano
		this.player = new IA(hand);

		// creamos la mano del cliente
		this.clientHand = new Pile(this.remainingPile.getAmount(HANDSIZE));

		// obtenemos la pieza que empieza
		this.startingPiece = getStartingPiece();

	}

	/**
	 * Function that returns the starting Piece
	 */
	public Piece getStartingPiece() {
		for (Piece p : this.getCatalog().getPieces()) {

			if (this.player.hasPiece(p) || clientHand.hasPiece(p)) {
				return p;
			}
		}
		// Siempre empezara una ficha, pero bueno...
		return null;
	}

	public void initGame() {

		while (STATE == State.PLAYING) {

			switch (ACTION) {

			case INIT:
				// comprobamos quien tiene la primera pieza y entregamos las
				// fichas y el sigiuiente movimiento

				if (this.player.hasPiece(this.startingPiece)) {
					this.comm.sendInitMovement(clientHand.getPieces(),
							new Movement(this.startingPiece, null));
				} else {
					// Enviamos un movement con ficha null
					this.comm.sendInitMovement(clientHand.getPieces(),
							new Movement(null, null));
				}

				break;
			case MOVE:
				analizeMovement();
				break;
			case ERROR:
				analizeError();
				break;
			case UNKNOWN:
				closeGame();
			default:
				break;

			}

			if (STATE != State.ENDGAME) { // si no se ha terminado el juego,
											// esperamos la siguiente accion del
											// cliente
				this.ACTION = this.comm.readHeader();
			}
		}

	}

	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.ENDGAME;

	}

	private void analizeMovement() {

		Movement rivalMove = this.comm.seeClientMovement();
		int handLength = this.comm.seeClientHandLength();

		// TODO ERROR HAND LENGTH
		if (rivalMove.isNT()) { // cliente no puede tirar
			this.STATE = State.CLIENTNP; // el estado pasa a ser que el cliente
											// no ha podido tirar

			if (this.remainingPile.getLength() > 0) { // aun hay piezas para
														// robar
				Piece piece = this.remainingPile.getRandomPiece(); // extraemos
																	// una pieza
																	// de la
																	// pila de
																	// pendientes
				givePiece(piece);
			} else { // no hay piezas para robar, miramos movimiento cliente

				if (this.player.hasMove(this.playedPile)) { // servidor puede
															// tirar
					makeMovement();

				} else { // servidor no puede tirar, no quedan fichas
					makeNoMovement();
				}
			}
		} else { // cliente puede tirar
			this.STATE = State.CLIENTMOVE; // el estado pasa a ser que el
											// cliente si ha podido tirar
			if (this.clientHand.hasPiece(rivalMove.getPiece())
					&& this.isValidMovement(rivalMove)) {// comprobamos si es
															// una accion valida
															// del cliente
				this.clientHand.deletePiece(rivalMove.getPiece());
				this.playedPile.addPiece(rivalMove.getPiece());

				// si el servidor puede tirar
				if (this.player.hasMove(this.playedPile)) { // servidor puede
															// tirar
					makeMovement();

				} else {

					// servidor no puede tirar
					// damos fichas al server hasta que pueda tirar o no queden
					// fichas
					while (!this.player.hasMove(this.playedPile)
							&& remainingPile.getLength() > 0) {
						this.player.setPiece(this.remainingPile
								.getRandomPiece());
					}

					if (!this.player.hasMove(this.playedPile)) { // si aun no
																	// puede
																	// tirar
						makeNoMovement();
					} else { // despues de robar si puede tirar
						makeMovement();
					}

				}
				;

			} else { // enviar error provocado por cliente

			}
		}

	}

	private void makeMovement() {
		// montamos el siguiente movimiento
		Movement newMove = this.player.nextMove(this.playedPile);
		// eliminamos la ficha del movimiento de la pila de jugados
		this.player.removePiece(newMove.getPiece());
		// enviamos la jugada con los datos necesarios
		this.comm.sendServerMovement(newMove, this.player.handLength(),
				this.remainingPile.getLength());

	}

	private void makeNoMovement() {
		if (this.STATE == State.CLIENTNP || this.STATE == State.SERVERNP) { // cliente
																			// no
																			// puede
																			// tirar
																			// y
																			// servidor
																			// tampoco
			this.STATE = STATE.ENDGAME;
			// TODO NEXT ACTION
		} else {
			this.STATE = State.SERVERNP;
			// montamos el siguiente movimiento que es un NT
			Movement newMove = new Movement(null, null);
			// enviamos la jugada con los datos necesarios
			this.comm.sendServerMovement(newMove, this.player.handLength(),
					this.remainingPile.getLength());
		}
	}

	private void givePiece(Piece piece) {

		this.comm.sendPieceToClient(piece);

	}

	private void analizeError() {

	}

	private void testServerDomino() {

		System.out.println("Catalog:\n" + getCatalogRepresentation());
		System.out.println("Client Hand:\n" + clientHand.getRepresentation());
		System.out.println("Server Hand:\n" + this.player.handRepresentation());
		System.out.println("Floor:\n" + remainingPile.getRepresentation());
		Piece startingPiece = getStartingPiece();
		System.out.println("Starting piece: "
				+ startingPiece.getRepresentation());
		if (clientHand.hasPiece(startingPiece))
			System.out.println("Empieza cliente");
		else
			System.out.println("Empieza server");

	}

}
