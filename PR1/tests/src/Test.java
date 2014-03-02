import java.util.Scanner;

import models.Catalog;
import models.Piece;
import models.Pile;
import models.PlayedPile;


public class Test {

	private static Scanner sc;
	private static String catalogDesc = "00010203040506111213141516222324252633343536444556555666";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		String input;
		int inputInt;
		Catalog catalog = new Catalog();
		
		// ya esta probado
		/*System.out.println("Insertemos fichas ex: 00 -> (0|0), 12 -> (1|2), 321(2|3) (99) para terminar:");
		System.out.println("Ficha a insertar:");
		
		while(!(input = sc.nextLine()).equals("99")){
			System.out.println("Creamos: " + input);
			System.out.println("Ficha a insertar:");
			catalog.addPiece(new Piece(input));
		}*/
		
		for(int i = 0; i < catalogDesc.length(); i += 2){
			char left = catalogDesc.charAt(i);
			char right = catalogDesc.charAt(i+1);
			catalog.addPiece(new Piece(left, right));
		}
		System.out.println("Catalogo actual ("+ catalog.getLength() +")");
		System.out.println(catalog.getRepresentation());
		
		// creamos la pila de fichas enla mesa
		Pile remainingPile = new Pile(catalog);
		System.out.println("Fichas en la mesa : ");
		System.out.println(remainingPile.getRepresentation());
		
		
		System.out.println("Cuantas piezas quieres en la mano? [int] : ");
		inputInt = (int)sc.nextInt();

		// Creamos una mano con x fichas
		Pile hand = new Pile();
		try{
			hand.addPieces(remainingPile.getAmount(inputInt));
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		System.out.println("Cuantas piezas quieres en la mesa? [int] : ");
		inputInt = (int)sc.nextInt();

		// Creamos una mano con x fichas
		PlayedPile floor = new PlayedPile();
		try{
			floor.addPieces(remainingPile.getAmount(inputInt));
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		
		System.out.println("Catalogo actual : ");
		System.out.println(catalog.getRepresentation());
		

		System.out.println("Fichas restantes : ");
		System.out.println(remainingPile.getRepresentation());
		
		System.out.println("Fichas jugadas : ");
		System.out.println(floor.getRepresentation());
		
		System.out.println("Mano : ");
		System.out.println(hand.getRepresentation());
		
		// limpiamos el buffer del sc
		if(sc.hasNextLine()){
			input = sc.nextLine();
		}
		
		while( hand.getLength() > 0){
			System.out.println("Selecciona una ficha de la mano para jugar: ");
			input = sc.nextLine();
			System.out.println("Quieres darle la vuelta a la ficha (0 no, 1 si): ");
			input += sc.nextLine();
			Piece nueva = new Piece(input);
	
			while( !(catalog.hasPiece(nueva) && hand.hasPiece(nueva)) ){
				System.out.println("Selecciona una ficha valida de la mano para jugar: ");
				input = sc.nextLine();
				System.out.println("Quieres darle la vuelta a la ficha (0 no, 1 si): ");
				input += sc.nextLine();
				nueva = new Piece(input);
			}
			
			System.out.println("Juegas por la izquierda o por la derecha: (l o r) ");
			while(!(input = sc.nextLine()).matches("[l|r|L|R]")){
				System.out.println("Introduce un lado valido... (l o r) ");
			}
			
			System.out.println("Has seleccionado " + nueva.getRepresentation());
			
			if(floor.matchSide(nueva, input)){
				floor.pushSide(nueva, input);
				hand.deletePiece(nueva);
				System.out.println("Jugada valida");
			}else {
				System.out.println("Jugada no valida");
			}
			
			
			

			System.out.println("\nFichas restantes : ");
			System.out.println(remainingPile.getRepresentation());
			
			System.out.println("\nFichas jugadas : ");
			System.out.println(floor.getRepresentation());
			
			
			System.out.println("\nTu mano queda asi");
			System.out.println(hand.getRepresentation());
		}
		System.out.println("\nFIN");
	}

}
