package models;

public class PlayedPile extends Catalog {
	
	public PlayedPile() {
		super();
	}
	
	public PlayedPile(Catalog anotherCatalog){
		super(anotherCatalog);
	}
	
	public PlayedPile(Piece[] pieces){
		super(pieces);
	}

	public void pushLeft (Piece piece) {
		this.pieces.addFirst(new Piece(piece));
	}

	
	public void pushRight (Piece piece) {
		this.pieces.addLast(new Piece(piece));
		
	}
}