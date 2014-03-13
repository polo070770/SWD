package controllers;

import models.Catalog;
import models.Movement;
import models.Piece;
import models.PlayedPile;
import models.Side;
import controllers.abstracts.Player;

public class Domino {

	private final String CATALOG = "66554433221100656463546253615243605142504132403130212010";

	protected Side LEFT = Side.LEFT;
	protected Side RIGHT = Side.RIGHT;
	protected final int HANDSIZE = 7;

	private Catalog catalog;
	protected PlayedPile playedPile;
	protected Player player;

	public Domino() {
		this.playedPile = new PlayedPile();
		catalog = new Catalog();
		fillCatalog(); // llenamos el catalogo de fichas
	};

	private void fillCatalog() {
		for (int i = 0; i < CATALOG.length(); i += 2) {
			char left = CATALOG.charAt(i);
			char right = CATALOG.charAt(i + 1);
			catalog.addPiece(new Piece(left, right));
		}
	}

	/** BOOLEANS **/

	/**
	 * Return if is a valid Domino piece. Valid domino piece has to be present
	 * within the current catalog
	 * 
	 * @param piece
	 * @return
	 */
	public boolean isValidPiece(Piece piece) {
		return catalog.hasPiece(piece);

	}

	/**
	 * Returns if a side id valid Valid side can be LEFT or RIGHT
	 * 
	 * @param side
	 * @return
	 */
	public boolean isValidSide(char side) {
		return Side.validSide(side);
	}

	/**
	 * Returns if a movement is valid within a domini context First of all, it
	 * checks if it has a valid piece, then it checks if movement has a valid
	 * side, then it checks if the piece isn't currently in the played pile and
	 * eventually checks if the movement matches with the current played pile
	 * 
	 * @param movement
	 * @return
	 */
	public boolean isValidMovement(Movement movement) {
		Piece movementPiece = movement.getPiece();
		return isValidPiece(movementPiece)
				&& !playedPile.hasPiece(movementPiece)
				&& playedPile.matchMovement(movement);
	}

	/**
	 * Returns a copy of the private catalog
	 * 
	 * @return
	 */
	public Catalog getCatalog() {
		return this.catalog.getCatalog();
	}

	public String getCatalogRepresentation() {
		return this.catalog.getRepresentation();
	}

}
