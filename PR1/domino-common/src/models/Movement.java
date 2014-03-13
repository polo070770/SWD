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
		this.piece = null;
		
		if(piece != null)this.piece = new Piece(piece);
		this.side = side;
	}
	
	
	public Movement(char[] chars) {
		if(chars[0] == 'N'){
			this.piece = null;
			this.side = null;
		}else{
			this.piece = new Piece(chars[0], chars[1], chars[2]);
			this.side = Side.fromChar(chars[3]);
		}
	}


	public boolean isNT(){
		return this.piece == null;
	}
	
	public boolean isNewPiece(){
		return this.side == Side.PADDING;
	}
	
	public Side getSide(){
		return this.side;
	}
	
	public Piece getPiece(){
		if(piece != null)return new Piece(this.piece);
		return null;
	}

	public String getRepresentation(){
		if(piece == null) return "NO PIECE";
		if(this.side == Side.LEFT){
			return "<--" + piece.getRepresentation();
		}
		return piece.getRepresentation() + "-->";
				
	}

}
