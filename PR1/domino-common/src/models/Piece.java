package models;

/**
 * This class defines a domino piece attributes and functions
 * @author swd
 *
 */
public class Piece {
	private final char LEFT = 'L';
	private final char RIGHT = 'R';

	
	private char left;
	private char right;
	private boolean reverse;
	
	private Piece(){}

	/**
	 * Creates a new piece from another
	 * @param anotherPiece
	 */
	public Piece(Piece anotherPiece) {
		this.left = anotherPiece.left;
		this.right = anotherPiece.right;
		this.reverse = anotherPiece.reverse;
		
	}
	/**
	 * Creates a new piece from params
	 * @param left
	 * @param right
	 * @param reverse
	 */
	public Piece (char left, char right, boolean reverse) {
		this.reverse = reverse;
		// Siempre guardamos la posicion original de la pieza
		// el reverse nos indica que a la hora de jugarla se opta 
		// por ponerla al revés
		if(this.reverse){
			this.left = right;
			this.right = left;
		}else{
			this.left = left;
			this.right = right;	
		}
	}

	/**
	 * Creates a new piece from params
	 * @param left
	 * @param right
	 * @param reverse
	 */
	public Piece (char left, char right, char reverse) {
		// calling to previous constructor
		this(left, right, reverse == '1');
	}
	/**
	 * Creates a new piece from params
	 * @param left
	 * @param right
	 */
	public Piece (char left, char right) {
		this(left, right, false);
	}
	/**
	 * Creates a new piece from a string definition.
	 * Length must be 2 or 3 
	 * L = charAt(0)
	 * R = charAt(1)
	 * optional reverse = charAt(2)
	 * @param definition
	 */
	public Piece(String definition){
		this();
		this.left = definition.charAt(0);
		this.right = definition.charAt(1);
		this.reverse = (definition.length() == 3 && definition.charAt(2) == '1');

	}
	/**
	 * Return the left side of a piece, if piece is reversed we return the right side
	 * @return
	 */
	public char getLeft () {
		if(this.reverse)return this.right; 
		return this.left;
		
	}
	/**
	 * Return the right side of a piece, if piece is reversed we return the left side
	 * @return
	 */
	public char getRight () {
		if(this.reverse)return this.left; 
		return this.right;
	}

	/**
	 * Return the unique identification
	 * @return
	 */
	public String getId () {
		String response = "";
		response += this.left + this.right;
		
		return response;
	}

	/**
	 * Returns true if current piece has the same values to another. Otherwise returns false  
	 * @param anotherPiece
	 * @return
	 */
	public boolean isEqual (Piece anotherPiece) {
		return this.left == anotherPiece.left && 
				this.right == anotherPiece.right;
	}

	/**
	 * Returns if current piece matches to another
	 * @param piece The piece to try to match with the current one
	 * @param side specifies the side of the piece that we want try to match. Must be char L or R
	 * @return
	 */
	public boolean matches (Piece piece, char side) {
		// hacemos un upper case de side
		side = Character.toUpperCase(side);

		// probamos si encaja por el lado izquierdo
		if ( side == LEFT ){
			return this.getLeft() == piece.getRight();
		}else if( side == RIGHT ){ 
			return this.getRight() == piece.getLeft();
		}
		
		return false;
	}

	/**
	 * Return the string representation of the piece
	 * @return
	 */
	public String getRepresentation () {
		String response = "[";
		return response + this.getLeft() + "|" + this.getRight() + "]";
		
		
	}

}
