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

	public boolean hasMove(PlayedPile playedPile) {
		for(Piece p : hand.getPieces()){
			if(playedPile.matchLeft(p) || playedPile.matchRight(p)){
				return true;
			}
		}
		return false;
	}

	public Movement nextMove(PlayedPile playedPile) {
		for(Piece p : hand.getPieces()){
			if(playedPile.matchLeft(p) || playedPile.matchRight(p)){
				Side side = playedPile.matchLeft(p) ? Side.LEFT : Side.RIGHT;
				Movement newMove = new Movement(p, side);
				return newMove;
			}
		}
		return null;
	}
	
	public void removePiece(Piece p){
		this.hand.deletePiece(p);
		
	}

	@Override
	public int handLength() {
		return this.hand.getLength();
	}

}
