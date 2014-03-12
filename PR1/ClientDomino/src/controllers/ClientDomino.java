package controllers;

import models.Movement;
import models.Piece;
import models.Pile;
import net.DominoLayer.Id;
import view.Human;
import controllers.net.Communication;

public class ClientDomino extends Domino {
	public enum State {
		CLIENTNP, SERVERNP, CLIENTMOVE, ERRORSUBMIT, ENDGAME, WAITING, PLAYING;
	}

	public enum Action {
		INIT, WAITNEXT, READERROR, SENDERROR, READMOVE, CLIENTMOVE, CLIENTERROR, SENDENDGAME, CLIENTNT, SENDPIECE, SENDMOVE, SERVERMOVE, SERVERNT, SENDNT;
	}

	private Pile remainingPile;
	private Pile clientHand;
	private Communication comm;
	private Piece startingPiece;

	private State STATE = State.PLAYING;
	private Action ACTION = Action.INIT;

	private Movement humanReply;
	private Movement serverReply;

	public ClientDomino(Communication comm) {
		super(); // create super resources
		this.comm = comm;
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

		/*
		 * 4 ultimos bytes que indican quien juega
		 */
		char[] movementChars = new char[4];
		movementChars[0] = resources[i++];
		movementChars[1] = resources[i++];
		movementChars[2] = resources[i++];
		movementChars[3] = resources[i++];

		serverReply = new Movement(movementChars);
		
		initGame();
		closeGame();

	}

	public void initGame() {

		while (this.STATE != State.ENDGAME) {
			switch (this.ACTION) {

			case WAITNEXT:
				// recibimos la contestacion del servidor

				Id id = this.comm.readHeader();
				ACTION = convertIdToAction(id);
				break;

			case INIT:
				// analizamos la respuesta del servidor
				if (serverReply.isNT()) {
					// es un NT-> servidor no puede tirar, empieza human
					humanReply = this.player.getFirstMovement();
					System.out.println(humanReply.getRepresentation());
					ACTION = Action.WAITNEXT;

				} else {
					// el servidor ha empezado a jugar primero, toca jugar a
					// human
					System.out.println(serverReply.getRepresentation());

					this.startingPiece = serverReply.getPiece();
					this.playedPile.addPiece(startingPiece);
					ACTION = Action.SENDMOVE;

				}

				break;
			case READMOVE:
				// leemos la contestacion del servior
				serverReply = new Movement(this.comm.readNextMovementChar());
				if (serverReply.isNT()) {
					// si el servidor no puede tirar, el cliente tira
					if (this.player.handLength() > 0) {
						// tira si aun tiene fichas en la mano
						ACTION = Action.SENDMOVE;
					} else {
						// sino contestara con un no puedo tirar
						ACTION = Action.SENDNT;
					}
				} else {
					// el servidor ha tirado una ficha, nos toca contestar
					System.out.println(serverReply.getRepresentation());
					this.playedPile.addPiece(serverReply.getPiece());
					ACTION = Action.SENDMOVE;

				}
				break;
				
			case SENDMOVE:
				humanReply = this.player.nextMove(playedPile);
				sendMovement(humanReply);
				ACTION = Action.WAITNEXT;
				
				break;
			case SENDNT:
				humanReply = new Movement(null, null);
				this.comm.sendClientMovement(humanReply, this.player.handLength());
				ACTION = Action.WAITNEXT;
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
			return Action.WAITNEXT;

		case ENDGAME: // la dominolayer nos puede indicar endgame debido a algun
						// error IO
			return Action.WAITNEXT;

		case MOVE: // el servidor hace move
			return Action.READMOVE;

		default:
			return Action.SENDENDGAME;
		}
	}

	/**
	 * Funcion que elimina la ficha de la pila del servidor y la envia cliente
	 * @param move
	 */
	private void sendMovement(Movement move){
		//la eliminamos
		this.player.removePiece(move.getPiece());
		//enviamos la jugada con los datos necesarios
		this.comm.sendClientMovement(move, this.player.handLength());
	}
	
	
	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.ENDGAME;

	}
}