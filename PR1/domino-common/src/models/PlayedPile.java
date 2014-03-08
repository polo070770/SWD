package models;

public class PlayedPile extends Catalog {
	static final char RIGHTSIDE = 'R';
	static final char LEFTSIDE = 'L';
	
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
	
	public void pushSide(Piece piece, char side){
		side = Character.toUpperCase(side);
		if(side == LEFTSIDE)pushLeft(piece);
		else pushRight(piece); 
	}
	
	public void pushSide(Piece piece, String side){
		pushSide(piece, side.charAt(0));
	}
	/**
	 * Returns if the specified piece matches with the pieces on the right side of the pile
	 * @param piece
	 * @return
	 */
	public boolean matchRight(Piece piece){
		return this.pieces.peekLast().matches(piece, RIGHTSIDE);
	}
	/**
	 * Returns if the specified piece matches with the pieces on the left side of the pile
	 * @param piece
	 * @return
	 */
	public boolean matchLeft(Piece piece){
		return this.pieces.peekFirst().matches(piece, LEFTSIDE);
	}
	/**
	 * Returns if the specified piece matches with the pieces on the specified side of the pile
	 * @param piece
	 * @return
	 */
	public boolean matchSide(Piece piece, char side){
		side = Character.toUpperCase(side);
		return (side == LEFTSIDE) ? matchLeft(piece) : matchRight(piece);
		
	}
	/**
	 * Returns if the specified piece matches with the pieces on the specified side of the pile
	 * @param piece
	 * @return
	 */
	public boolean matchSide(Piece piece, String side){
		return matchSide(piece, side.charAt(0));
		
	}
	
	/** 
	 * Returns if the specified movement matches with the pieces on the pile
	 * @param movement
	 * @return
	 */
	public boolean matchMovement(Movement movement){
		return matchSide(movement.getPiece(), movement.getSide());
	}
	
	/**
	 * Returns a copy of the current playedPile
	 * @return
	 */
	public PlayedPile getPlayedPile(){
		return new PlayedPile(this.getCatalog());
		
	}
}