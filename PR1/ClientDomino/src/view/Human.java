package view;

import java.util.Scanner;

import models.Catalog;
import models.Movement;
import models.Piece;
import models.Pile;
import models.PlayedPile;
import models.Side;
import controllers.abstracts.Player;

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
		for (Piece p : hand.getPieces()) {
			if (playedPile.matchLeft(p) || playedPile.matchRight(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removePiece(Piece p) {
		this.hand.deletePiece(p);
	}

	@Override
	public int handLength() {
		return this.hand.getLength();
	}

	public Movement getFirstMovement() {
		String input;
		System.out.println("- Fichas Cliente: ");
		System.out.println(hand.getRepresentation());

		System.out.println("- Segun las reglas, empiezas tu.");
		System.out
				.println("- Selecciona una ficha de la mano para jugar ex( 03 ): ");
		input = sc.nextLine();
		input += "0"; // la primera tirada no se puede girar
		Piece nueva = new Piece(input);

		while (!(catalog.hasPiece(nueva) && hand.hasPiece(nueva))) {
			System.out
					.println("- Selecciona una ficha valida de la mano para jugar: ");
			input = sc.nextLine();
			input += "0";// la primera tirada no se puede girar
			nueva = new Piece(input);
		}

		Movement nextMovement = new Movement(nueva, Side.RIGHT);

		return nextMovement;

	}

	@Override
	public Movement nextMove(PlayedPile playedPile) {

		String input;
		System.out.println("Estas son tus fichas");
		System.out.println(hand.getRepresentation());

		System.out.println("Es tu turno!");

		System.out
				.println("Selecciona una ficha de la mano para jugar ex( 03 ): ");
		input = sc.nextLine();

		Piece nueva = new Piece(input.charAt(0), input.charAt(1));

		while (!(catalog.hasPiece(nueva) && hand.hasPiece(nueva))) {
			System.out
					.println("Selecciona una ficha valida de la mano para jugar: ");
			input = sc.nextLine();
			nueva = new Piece(input.charAt(0), input.charAt(1));
		}

		System.out.println("Quieres girar la ficha? (y/n)");
		if (sc.nextLine().equalsIgnoreCase("y")) {
			// input = new StringBuffer(input).reverse().toString();
			nueva.setReverse(true);
		}

		System.out.println("En que lado de la mesa quieres poner la ficha?? ");
		System.out.println("-Lado izquierda->L\n-Lado derecho->R");
		input = sc.nextLine();

		while (!(input.equalsIgnoreCase("L") || input.equalsIgnoreCase("R"))) {
			System.out
					.println("Selecciona el lado de la mesa donde quieres poner la ficha. ");
			System.out.println("-Lado izquierda->L\n-Lado derecho->R");
			input = sc.nextLine();
		}

		Movement movement = new Movement(nueva, Side.LEFT);

		if (input.equalsIgnoreCase("R"))
			movement = new Movement(nueva, Side.RIGHT);

		return movement;
	}

}
