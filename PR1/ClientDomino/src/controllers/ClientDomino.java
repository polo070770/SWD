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
		// comprobamos la jugada del servidor
		if (serverReply.isNT()) {
			// es un NT-> servidor no puede tirar, empieza human
			humanReply = this.player.getFirstMovement();
			System.out.println(humanReply.getRepresentation());

		} else {
			// el servidor ha empezado a jugar primero, toca jugar a human
			System.out.println(serverReply.getRepresentation());

			this.startingPiece = serverReply.getPiece();
			this.playedPile.addPiece(startingPiece);

		}

		this.STATE = State.PLAYING;
		
		closeGame();

	}
	
	public void initGame() {

		while (this.STATE == State.PLAYING) {
			switch (this.ACTION) {
			}
		}

	}

	

	private void closeGame() {
		this.comm.closeConnection();
		this.STATE = State.ENDGAME;

	}
}