package models;

import java.util.Iterator;
import java.util.Random;

import models.exceptions.PileException;


public class Pile extends Catalog {
	Random rand = new Random();
	
	public Pile() {
		super();
	}
	
	public Pile(Catalog anotherCatalog){
		super(anotherCatalog);
	}
	public Pile(Piece[] pieces){
		super(pieces);
	}

	/**
	 * Deletes the specified piece from the pile
	 * I searches the piece and compares his values using isEqual method
	 * @param piece
	 */
	public void deletePiece (Piece piece) {
		for(Iterator<Piece> it = this.pieces.iterator(); it.hasNext(); ) {
		    if(it.next().isEqual(piece)) { 
		        it.remove(); 
		        break;
		        }
		 } 
	}
	
	/**
	 * Return a piece and removes it from the pile
	 * @return
	 */
	public Piece getOne () {
		return this.pieces.poll();
	}
	
	/**
	 * Returns a random piece and removes it from the pile
	 * @return
	 */
	public Piece getRandomOne () throws PileException{
		int randomNumber = rand.nextInt(this.pieces.size());
		Piece piece = null;
		//System.out.println("random: " + randomNumber);
		//System.out.println("size: " + this.pieces.size());
		// recorremos el listado de piezas
		for(Iterator<Piece> it = this.pieces.iterator(); it.hasNext(); randomNumber--) {
			// si el numero ha llegado a 0 es la pieza que buscamos
			if (randomNumber == 0){
				//generamos una pieza igual
				piece = new Piece(it.next());
				//removemos el contenido del iterador eliminando asi la pieza de la pila
				it.remove(); 
				// salimos
		        break;
			}
			it.next();
		}
		return piece;
	}
	
	/**
	 * Returns a random piece and removes it from the pile
	 * @return
	 */
	public Piece getRandomPiece () {
		Piece piece = null;
		try{
			piece = getRandomOne();
		}catch(PileException e){
			System.out.println(e.getMessage());
		}
		
		return piece;
	}
	
	
	
	/**
	 * Returns random array with the specified value
	 * @param number
	 * @return
	 * @throws PileException
	 */
	public Piece[] getAmount(int number) {
		// controlamos las posibles excepciones
		Piece[] pieces = null;
		try{
			//array de salida
			pieces = new Piece[number];
			
			// buscamos piezas al azar una a una
			while(number > 0) pieces[--number] = getRandomOne();
			
			return pieces;
		}catch(PileException e){
			System.out.println(e.getMessage());
		}
		return pieces;
	}
	
	
	/**
	 * Returns a copy of the current pile
	 * @return
	 */
	public Pile getPile(){
		return new Pile(this.getCatalog());
	}
}

