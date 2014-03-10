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

	private Pile remainingPile;
	private Pile clientHand;
	private Communication comm;
	private Piece startingPiece;

	private State STATE = State.PLAYING;
	private Id ACTION = Id.INIT;

	private Movement firstMovement;
	private Movement humanMovement;

	public ClientDomino(Communication comm) {
		super(); // create super resources
		this.comm = comm;
		createClientResources();
	}

	private void initGame() {

	}

	private void createClientResources() {
		Pile hand = new Pile();
		// if(this.comm.readHeader() == Id.INIT){
		char[] resources = this.comm.readInitMovementChar();

		// creamos los recursos del cliente a partir de los datos recibidos
		int i;

		for (i = 0; i < this.HANDSIZE * 2; i += 2) {
			// fichas de inicio recibidias para el cliente
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

		firstMovement = new Movement(movementChars);
		// comprobamos la jugada del servidor
		if (firstMovement.isNT()) {
			// es un NT-> servidor no puede tirar, empieza el jugador
			humanMovement = this.player.getFirstMovement();
			System.out.println(humanMovement.getRepresentation());
		} else {
			// el servidor ha empezado a jugar primero, toca jugar al jugador
			System.out.println(firstMovement.getRepresentation());

			this.startingPiece = firstMovement.getPiece();
			this.playedPile.addPiece(startingPiece);
			
			this.player.nextMove(playedPile);

		}

		closeGame();

	}

	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.ENDGAME;

	}
}