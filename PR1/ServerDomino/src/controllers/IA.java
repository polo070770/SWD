package controllers;

import models.Catalog;
import models.Movement;
import models.Piece;
import models.Pile;
import models.PlayedPile;
import models.Side;
import controllers.abstracts.Player;

public class IA extends Player {

	public IA(Catalog catalog) {
		super(catalog);
	}

	public IA(Pile hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}


	/**
	 * funcion que devuelve el siguiente movimiento a realizar
	 */
	public Movement nextMove(PlayedPile playedPile) {
		for (Piece p : hand.getPieces()) {
			// comprueba si la ficha encaja por la izquiera o por la derecha
			if (playedPile.matchLeft(p) || playedPile.matchRight(p)) {
				// si encaja, comprueba por que lado y devuelve el movimiento
				Side side = playedPile.matchLeft(p) ? Side.LEFT : Side.RIGHT;
				Movement newMove = new Movement(p, side);
				return newMove;
			}
			// sino, gira la pieza y vuelve a comprobar lo mismo
			System.out.println("doy vuelta la ficha");
			p.reverse();
			if (playedPile.matchLeft(p) || playedPile.matchRight(p)) {
				Side side = playedPile.matchLeft(p) ? Side.LEFT : Side.RIGHT;
				Movement newMove = new Movement(p, side);
				return newMove;
			}
		}
		return null;
	}



}
