package controllers.abstracts;

import models.Catalog;
import models.Movement;
import models.Piece;
import models.Pile;
import models.PlayedPile;

public abstract class Player {
	protected Pile hand;

	
	public Player(Pile hand) {
		this.hand = hand;
	}

	public Player(Catalog catalog){
		this.hand = new Pile(catalog);
	}

	public void setPiece(Piece piece) {
		this.hand.addPiece(piece);
	}
	public boolean hasPiece(Piece piece){
		return this.hand.hasPiece(piece);
	}
	public String handRepresentation(){
		return hand.getRepresentation();
	}

	public Movement getFirstMovement(){
		return null;
	};
	
	/**
	 * funcion que devuelve si hay una tirada posible
	 * @param playedPile
	 * @return
	 */
	public boolean hasMove(PlayedPile playedPile) {
		for (Piece p : hand.getPieces()) {
			// comprueba si la ficha actual encaja por la izquierda o por la derecha
			if (playedPile.matchLeft(p) || playedPile.matchRight(p)) {
				return true;
			}
			// sino, gira la ficha actual y vuelve a probar lo mismo
			p.reverse();
			if (playedPile.matchLeft(p) || playedPile.matchRight(p)) {
				return true;
			}
		}
		return false;
	}

	public void removePiece(Piece p) {
		this.hand.deletePiece(p);

	}


	public int handLength() {
		return this.hand.getLength();
	}
	
	public abstract Movement nextMove(PlayedPile playedPile);
	
	

}
