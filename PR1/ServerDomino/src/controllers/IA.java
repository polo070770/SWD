package controllers;

import models.Catalog;
import models.Piece;
import models.Pile;
import controllers.abstracts.Player;

public class IA extends Player {

	public IA(Catalog catalog) {
		super(catalog);
	}
	public IA(Pile hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasMove() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Piece nextMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
