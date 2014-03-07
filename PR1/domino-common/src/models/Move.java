package models;

public class Move {

	private char side;
	private Piece piece;
	
	public Move(Piece piece, char side){
		this.piece = new Piece(piece);
		this.side = side;
	}
	
	
	public char getSide(){
		return this.side;
	}
	
	public Piece getPiece(){
		return new Piece(this.piece);
	}

}
