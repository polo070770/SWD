package models;

/**
 * This class defines a movement.
 * Contains a piece and a side that represents the side of the floor where match the piece
 * @author swd
 *
 */
public class Movement {
	private final char LEFT = 'L';
	private final char RIGHT = 'R';
	
	private char side;
	private Piece piece;
	
	public Movement(Piece piece, char side){
		this.piece = new Piece(piece);
		this.side = Character.toUpperCase(side);;
	}
	
	
	public char getSide(){
		return this.side;
	}
	
	public Piece getPiece(){
		return new Piece(this.piece);
	}

	public String getRepresentation(){
		if(this.side == LEFT){
			return "<-----" + piece.getRepresentation();
		}
		return piece.getRepresentation() + "----->";
				
	}

}
