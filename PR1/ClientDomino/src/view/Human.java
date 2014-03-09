package view;
import java.util.Scanner;

import models.Catalog;
import models.Movement;
import models.Piece;
import models.Pile;
import models.PlayedPile;
import models.Side;
import controllers.abstracts.Player;
import java.util.Scanner;

public class Human extends Player {
	
	private Scanner sc;
	private Catalog catalog;
	public Human(Pile hand, Catalog catalog) {
			super(hand);
			this.catalog = catalog;
			sc = new Scanner(System.in);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasMove(PlayedPile playedPile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Movement nextMove(PlayedPile playedPile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePiece(Piece p) {
		// TODO Auto-generated method stub

	}

	@Override
	public int handLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Movement getFirstMovement(){
		String input;
		System.out.println("Estas son tus fichas");
		System.out.println(hand.getRepresentation());
		
		System.out.println("Segun las reglas, empiezas tu.");
		System.out.println("Selecciona una ficha de la mano para jugar ex( 03 ): ");
		input = sc.nextLine();
		input += "0"; // la primera tirada no se puede girar
		Piece nueva = new Piece(input);
		
		while( !(catalog.hasPiece(nueva) && hand.hasPiece(nueva)) ){
			System.out.println("Selecciona una ficha valida de la mano para jugar: ");
			input = sc.nextLine();
			input += "0";// la primera tirada no se puede girar
			nueva = new Piece(input);
		}
		
		Movement nextMovement = new Movement(nueva, null );
		return nextMovement;
		
		
		
	}

}
