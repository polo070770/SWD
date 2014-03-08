package controllers;

import models.Piece;
import models.Pile;
import models.exceptions.PileException;
import net.DominoLayer.Id;

public class ServerDomino extends Domino {
	
	private Pile remainingPile;
	private Pile clientHand;
	
	public ServerDomino(){
		super(); // create super resources
		createDominoResources(); //creamos los recursos del juego
		
		testServerDomino();
		

		
	}
	
	/**
	 * Function that creates the different resources for the domino game
	 */
	private void createDominoResources(){
		// creamos la pila de fichas pendientes a partir del catalogo
		this.remainingPile = new Pile(this.getCatalog());

		// creamos la mano del jugador
		Pile hand = new Pile(this.remainingPile.getAmount(HANDSIZE));

		// creamos el jugador y le asignamos su mano
		this.player = new IA(hand);
		
		// creamos la mano del cliente
		clientHand = new Pile(this.remainingPile.getAmount(HANDSIZE));
		
	}
	
	
	/**
	 * Function that returns the starting Piece
	 */
	public Piece getStartingPiece(){
		for(Piece p : this.getCatalog().getPieces()){
			
			if(this.player.hasPiece(p) || clientHand.hasPiece(p)){
				return p;
			}
		}
		// Siempre empezara una ficha, pero bueno...
		return null;
	}
	
	
	public void testServerDomino(){
		
		System.out.println("Catalog:\n" + getCatalogRepresentation());
		System.out.println("Client Hand:\n" + clientHand.getRepresentation());
		System.out.println("Server Hand:\n" + this.player.handRepresentation());
		System.out.println("Floor:\n" + remainingPile.getRepresentation());
		Piece startingPiece = getStartingPiece();
		System.out.println("Starting piece: " + startingPiece.getRepresentation());
		if(clientHand.hasPiece(startingPiece)) System.out.println("Empieza cliente");
		else System.out.println("Empieza server");
		
	}
}
