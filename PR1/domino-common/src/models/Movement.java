package models;

/**
 * This class defines a movement.
 * Contains a piece and a side that represents the side of the floor where match the piece
 * @author swd
 *
 */
public class Movement {
	private Side LEFT = Side.LEFT;
	private Side RIGHT = Side.RIGHT;
	
	private Side side;
	private Piece piece;
	
	public Movement(Piece piece, Side side){
		this.piece = new Piece(piece);
		this.side = side;
	}
	
	
	public Side getSide(){
		return this.side;
	}
	
	public Piece getPiece(){
		return new Piece(this.piece);
	}

	public String getRepresentation(){
		if(this.side == Side.LEFT){
			return "<-----" + piece.getRepresentation();
		}
		return piece.getRepresentation() + "----->";
				
	}

}
