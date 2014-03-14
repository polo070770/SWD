package controllers;

import models.DomError;
import models.Movement;
import models.Piece;
import models.Pile;
import models.Side;
import net.DominoLayer.Id;
import controllers.net.Communication;

/**
 * clase principal del juego
 * 
 * @author swd
 * 
 */
public class ServerDomino extends Domino {
	public enum Stater {
		CLIENTNP, SERVERNP, CLIENTMOVE, ERRORSUBMIT, ENDGAME, WAITING, PLAYING;
	}

	public enum State { // estados que pueden darse en el juego
		PLAYING, // jugando normal
		CLIENTERROR, // el cliente ha cometido un error y se le ha notificado

		ERRORHANDLED, // el cliente dice que hemos cometido un error y lo hemos
						// tratado de solucionar
		GAMEENDED, // el juego ha terminado
		CLIENTNT, // el cliente no puede tirar
		SERVERNT; // el servidor no puede tirar
	}

	public enum Action {
		INIT, WAITNEXT, READERROR, SENDERROR, READMOVE, CLIENTMOVE, CLIENTERROR, SENDENDGAME, CLIENTNT, SENDPIECE, SENDMOVE, SERVERMOVE, SERVERNT, SENDNT;
	}

	private final boolean INFO = true;
	protected final boolean LOG = true;

	private Pile remainingPile;
	private Pile clientHand;

	private Communication comm;
	private Piece startingPiece;

	private State STATE;
	private Action ACTION;

	/** CURRENT GLOBALS **/

	private String currentErrDescription;
	private int currentErrDescriptionNumber;

	private DomError currentError;

	/** CURRENT CLIENT GLOBALS **/
	private Movement currentClientMove;
	private int currentClientHandLength;
	private Piece currentClientSentPiece;
	/** CURRENT SERVER GLOBALS **/
	private Movement currentServerMove;

	public ServerDomino(Communication comm) {
		super(); // create super resources
		this.comm = comm;
		STATE = State.PLAYING; // estamos en el estado de jugar
		ACTION = Action.INIT; // la accion que toca es la de inicio de partida
		createDominoResources(); // creamos los recursos del juego
	}

	/**
	 * Function that creates the different resources for the domino game: -
	 * Remaining Pile - Server Hand - Client Hand - Server player - Get the
	 * first chip to start
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

		while (STATE != State.GAMEENDED) {

			switch (ACTION) {

			// WAIT CASE
			case WAITNEXT:
				// recibimos la operacion del cliente
				// /COMMUNICATION
				Id id = this.comm.readHeader();
				ACTION = convertIdToAction(id);
				break;

			// / INIT CASE
			case INIT:
				// comprobamos quien tiene la primera pieza y entregamos las
				// fichas y el siguiente movimiento
				if (this.clientHand.hasPiece(this.startingPiece)) {
					if (INFO)
						System.out.println(this.comm.getScocketDescription()
								+ " empieza client");
					// empieza el cleinte, enviamos un movement con ficha null
					this.comm.sendInitMovement(clientHand.getPieces(),
							new Movement(null, null));
				} else {
					if (INFO)
						System.out.println(this.comm.getScocketDescription()
								+ " empieza server con "
								+ this.startingPiece.getRepresentation());
					// empieza el servidor, enviamos el primer movimiento, con
					// las piezas del cliente
					this.comm.sendInitMovement(clientHand.getPieces(),
							new Movement(this.startingPiece, null));

					// quitamos la ficha inicial de la mano del servidor
					this.player.removePiece(this.startingPiece);
					this.playedPile.pushSide(this.startingPiece, Side.LEFT);
				}
				ACTION = Action.WAITNEXT;
				break;

			// / READ CASES
			case READMOVE:
				// /COMMUNICATION

				// Leemos la respuesta del cliente, ficha tirada y numero de
				// fichas restantes en su mano
				this.currentClientMove = this.comm.seeClientMovement();
				this.currentClientHandLength = this.comm.seeClientHandLength();

				if (INFO) {
					System.out.println(this.comm.getScocketDescription()
							+ " : "
							+ this.currentClientMove.getRepresentation());
					System.out.println(this.comm.getScocketDescription()
							+ " : " + this.currentClientHandLength + " fichas");
				}

				// Analizamos la tirada del cliente
				if (this.currentClientHandLength == 0
						&& this.clientHand.getLength() > 1) {
					// Primeramente comprobamos si dice que le quedan 0 fichas
					this.currentError = new DomError(1,
							"Numero de fichas pendientes no valido, aun te quedan "
									+ this.clientHand.getLength());
					ACTION = Action.CLIENTERROR;

				} else if (this.currentClientMove.isNT()) {
					// comprobamos si cliente no puede tirar, es un nt
					ACTION = Action.CLIENTNT;
				} else {
					// sino el cliente ha tirado
					ACTION = Action.CLIENTMOVE;
				}

				break;

			case READERROR:
				// TODO por ahora el servidor tira
				ACTION = Action.SERVERMOVE;
				break;

			// // ACTION DEFINE CASES
			case CLIENTMOVE:
				if (INFO) {
					System.out.println(this.comm.getScocketDescription()
							+ " hace movimiento ");

					System.out.println("Longitude de played pile :"
							+ this.playedPile.getLength());
				}

				if (this.playedPile.getLength() == 0
						&& this.clientHand.hasPiece(this.currentClientMove
								.getPiece())) {
					// si es el primer movimiento
					this.clientHand.deletePiece(this.currentClientMove
							.getPiece());
					// la insertamos en el lado correspondiente de la pila de
					// fichas jugadas
					this.playedPile.pushSide(this.currentClientMove.getPiece(),
							Side.LEFT);

					// toca jugar al servidor
					ACTION = Action.SERVERMOVE;

				} else if (this.clientHand.hasPiece(this.currentClientMove
						.getPiece())
						&& this.isValidMovement(this.currentClientMove)) {
					// si es una ficha que esta en la mano del cliente y es un
					// movimiento valido eliminamos la ficha de la mano del
					// cliente
					this.clientHand.deletePiece(this.currentClientMove
							.getPiece());
					// la insertamos en el lado correspondiente de la pila de
					// fichas jugadas
					this.playedPile.pushSide(this.currentClientMove.getPiece(),
							this.currentClientMove.getSide());

					// toca jugar al servidor
					ACTION = Action.SERVERMOVE;

				} else {
					// especificamos el error
					this.currentError = new DomError(2, "Jugada "
							+ this.currentClientMove.getRepresentation()
							+ " no valida ");
					ACTION = Action.CLIENTERROR;
				}

				break;

			case CLIENTNT:
				// El cliente comunica que no puede tirar
				if (STATE == State.SERVERNT) {
					// venimos de un estado en el que
					// el servidor no podia tirar
					ACTION = Action.SENDENDGAME;
				} else if (remainingPile.getLength() == 0) {
					// si no hay fichas para dar al cliente, tira el servidor
					STATE = State.CLIENTNT;
					ACTION = Action.SERVERMOVE;
				} else {
					// hay fichas para robar, enviamos ficha al cliente
					ACTION = Action.SENDPIECE;
				}
				break;

			case CLIENTERROR:
				if (STATE == State.CLIENTERROR) {
					// Si el cliente nos vuelve a enviar un error
					ACTION = Action.SENDENDGAME;

				} else {
					// Sino, enviamos un error al cliente
					ACTION = Action.SENDERROR;
				}
				break;

			case SERVERMOVE:
				if (INFO) {
					System.out.println("Estado tablero: "
							+ this.playedPile.getRepresentation());
					System.out.println("Fichas servidor: "
							+ this.player.handRepresentation());
				}

				if (this.player.hasMove(this.playedPile)) {
					// si el servidor puede tirar, tiene movimiento valido
					this.currentServerMove = this.player
							.nextMove(this.playedPile);
					ACTION = Action.SENDMOVE;
				} else {
					// servidor no puede tirar
					// damos fichas al server hasta que pueda tirar o no queden
					// fichas
					while ((!this.player.hasMove(this.playedPile))
							&& remainingPile.getLength() > 0) {
						Piece p = this.remainingPile.getRandomPiece();
						this.player.addPieceHand(p);
						if (INFO) {
							System.out.println("Servidor roba "
									+ p.getRepresentation());

						}
					}

					if (this.player.hasMove(this.playedPile)) {
						// Si al final ha encontrado ficha valida para tirar
						this.currentServerMove = this.player
								.nextMove(this.playedPile);
						ACTION = Action.SENDMOVE;
					} else {
						// Si no ha encontrado ficha aun despues de robar
						ACTION = Action.SERVERNT;
					}
				}
				break;

			case SERVERNT:
				if (STATE == State.CLIENTNT) {
					// Si ni cliente ni server pueden tirar, acaba juego
					ACTION = Action.SENDENDGAME;
				} else {
					// Sino enviamos al cliente un no puedo tirar
					ACTION = Action.SENDNT;
				}
				break;

			// SEND CASES
			case SENDMOVE:
				if (INFO)
					System.out.println("Servidor juega con "
							+ this.currentServerMove.getRepresentation());

				// insertamos la ficha en la pila
				this.playedPile.pushSide(this.currentServerMove.getPiece(),
						this.currentServerMove.getSide());
				// Enviamos movimiento al cliente
				sendMovement(this.currentServerMove);
				if (this.player.handLength() == 0) {
					// Si ya no le quedan mas fichas al servidor, entonces
					// servidor gana
					ACTION = Action.SENDENDGAME;
				} else {
					// Sino, continuamos jugando y esperamos contestacion
					// del cliente
					STATE = State.PLAYING;
					ACTION = Action.WAITNEXT;
				}
				break;

			case SENDNT:
				STATE = State.SERVERNT;
				// construimos el siguiente movimiento que es un NT
				this.currentServerMove = new Movement(null, null);

				// enviamos la jugada con los datos necesarios
				this.sendMovement(this.currentServerMove);
				
				/* deprecated
				 * this.comm.sendServerMovement(this.currentServerMove)
						this.player.handLength(),
						this.remainingPile.getLength());
				*/
				// esperamos respuesta del servidor
				ACTION = Action.WAITNEXT;
				break;

			case SENDPIECE:
				// Si el cliente no puede tirar, tenemos que darle ficha
				this.currentClientSentPiece = this.remainingPile
						.getRandomPiece(); // extraemos una pieza de la pila de
											// pendientes

				if (INFO)
					System.out.println("Servidor envia "
							+ this.currentClientSentPiece.getRepresentation()
							+ " que solicitaba ficha");

				this.clientHand.addPiece(this.currentClientSentPiece);
				this.sendMovement(new Movement(this.currentClientSentPiece, null));
				//sendPieceToClient();
				STATE = State.PLAYING; // Seguimos jugando
				ACTION = Action.WAITNEXT; // Esperamos contestacion del cliente
				break;

			case SENDERROR:
				// Enviamos error al cliente
				sendErrorToClient();
				// Nos guardamos el estado que el cliente ha tenido error
				STATE = State.CLIENTERROR;
				ACTION = Action.WAITNEXT; // Espramos contestacion del cliente
				break;

			case SENDENDGAME: // Aqui no hace falta asiganr accion
				sendEndGameToClient();
				STATE = State.GAMEENDED;
				break;
			}
		}// ENDWHILE
	}

	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.GAMEENDED;
	}

	// //// SEND FUNCTIONS

	private void sendPieceToClient() {
		this.comm.sendPieceToClient(this.currentClientSentPiece);

	}

	/**
	 * funcion que devuelve un error al cliente
	 */
	private void sendErrorToClient() {
		if (INFO) {
			System.out.println(this.comm.getScocketDescription() + " ERROR");
			System.out.println(this.currentError.getRepresentation());
		}
		this.comm.sendErrorToClient(this.currentError);
		/*
		 * Informamos la cliente de que ha producido un error
		 */

		// TODO

	}

	private void sendEndGameToClient() {
		this.comm.sendEndGameToClient(this.clientHand.getLength(),
				this.player.handLength());
		;

	}

	/**
	 * Funcion que elimina la ficha de la pila del servidor y la envia cliente
	 * 
	 * @param move
	 */
	private void sendMovement(Movement move) {
		// la eliminamos de la mano del serverr
		//si no es un NT
		if( STATE != State.SERVERNT && STATE != State.CLIENTNT){
			this.player.removePiece(move.getPiece());
		}
		// enviamos la jugada con los datos necesarios
		this.comm.sendServerMovement(move, this.player.handLength(),
				this.remainingPile.getLength());
	}

	// ///// CONVERTERS

	/**
	 * @param id
	 *            Funcion que devuelve una accion a realizar a partir de una id
	 * @return
	 */
	private Action convertIdToAction(Id id) {
		switch (id) { // parseamos la id recibida con su accion

		case TIMEOUT:// si es timeout no pasa nada
			return Action.WAITNEXT;

		case ERROR: // si el cliente nos dice que es error, lo analizamos
			return Action.READERROR;

		case ENDGAME: // la dominolayer nos puede indicar endgame debido a algun
						// error IO
			return Action.SENDENDGAME;

		case MOVE: // el cliente hace move
			return Action.READMOVE;

		default:
			return Action.SENDENDGAME;
		}
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
