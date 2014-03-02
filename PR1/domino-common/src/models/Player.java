package models;

public abstract class Player {
	private Pile hand;

	
	public Player(Pile hand) {
		this.hand = hand;
	}

	public Player (Catalog catalog){
		this.hand = new Pile(catalog);
	}
	
	public abstract boolean hasMove();

	public abstract Piece nextMove();

	public void takePiece(Piece piece) {
		this.hand.addPiece(piece);
	}
}
