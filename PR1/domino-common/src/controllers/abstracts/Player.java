package controllers.abstracts;

import models.Catalog;
import models.Piece;
import models.Pile;

public abstract class Player {
	private Pile hand;

	
	public Player(Pile hand) {
		this.hand = hand;
	}

	public Player(Catalog catalog){
		this.hand = new Pile(catalog);
	}
	
	public abstract boolean hasMove();

	public abstract Piece nextMove();

	public void setPiece(Piece piece) {
		this.hand.addPiece(piece);
	}
	public boolean hasPiece(Piece piece){
		return this.hand.hasPiece(piece);
	}
	public String handRepresentation(){
		return hand.getRepresentation();
	}

}
