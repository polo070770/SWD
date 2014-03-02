package models;

import java.util.Deque;
import java.util.LinkedList;
/**
 * The Catalog class represents a basic catalog container.
 * It defines a linked list with wrapper methods and other ones
 * for a representation purposes.
 * @author swd
 *
 */
public class Catalog {
	protected Deque<Piece> pieces;

	/**
	 * Creates an instance of a Catalog with and empty linkedlist.
	 */
	public Catalog() {
		this.pieces = new LinkedList<Piece>();
	}

	public Catalog (Piece piece) {
		this();
		
		this.pieces.add(piece);
		
	}

	public Catalog (Catalog anotherCatalog) {
		this();
	
		for(Piece piece : anotherCatalog.pieces) {
		    this.pieces.addLast(new Piece(piece));
		}
		
	}
	public Catalog (Piece[] pieces) {
		this();
		this.addPieces(pieces);
		
	}

	/**
	 * Returns if the specified piece exists in the current pieces list.
	 * The piece is evaluated by his parameters and not as the same object
	 * @param piece
	 * @return
	 */
	public boolean hasPiece (Piece piece) {
		for(Piece currentPiece : this.pieces){
			if(currentPiece.isEqual(piece)) return true;
		}
		return false;
	}
	
	/**
	 * Returns if the specified piece exists in the current pieces list.
	 * The piece is evaluated by his parameters and not as the same object
	 * @param left
	 * @param right
	 * @param reverse boolean
	 * @return
	 */
	public boolean hasPiece (char left, char right, boolean reverse) {
		return this.hasPiece(new Piece(left, right, reverse));
	}
	
	/**
	 * Returns if the specified piece exists in the current pieces list.
	 * The piece is evaluated by his parameters and not as the same object
	 * @param left
	 * @param right
	 * @param reverse char
	 * @return
	 */
	public boolean hasPiece (char left, char right, char reverse) {
		return this.hasPiece(new Piece(left, right, reverse));
	}


	public int getLength () {
		return this.pieces.size();
	}

	/**
	 * Inserts a copy of a specified piece into the current catalog
	 * @param piece
	 */
	public void addPiece (Piece piece) {
		this.pieces.addLast(new Piece(piece));
	}

	/**
	 * Inserts a copy of the specified pieces into the current catalog
	 * @param pieces Array of pieces
	 */
	public void addPieces (Piece[] pieces) {
		for(Piece piece : pieces){
			this.addPiece(piece);
		}
	}

	/**
	 * Returns a string representation of the current catalog
	 * @return
	 */
	public String getRepresentation () {
		String response = "";
		for(Piece currentPiece : this.pieces){
			response += currentPiece.getRepresentation();
		}
		return response;
	}
		
}