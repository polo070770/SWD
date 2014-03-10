package controllers;

import net.DominoLayer.Id;
import models.Movement;
import models.Piece;
import models.Pile;


import controllers.abstracts.Player;
import controllers.net.Communication;

public class ServerDomino extends Domino {
	public enum Stater{
		CLIENTNP, SERVERNP, CLIENTMOVE, ERRORSUBMIT, ENDGAME, WAITING, PLAYING;
	}
	
	public enum State{ //estados que pueden darse en el juego
		PLAYING, //jugando normal 
		CLIENTERROR, //el cliente ha cometido un error y se le ha notificado
		ERRORHANDLED, //el cliente dice que hemos cometido un error y lo hemos tratado de solucionar
		GAMEENDED,  // el juego ha terminado
		CLIENTNT, // el cliente no puede tirar
		SERVERNT; // el servidor no pùede tirar
	}
	public enum Action{
		INIT,
		WAITNEXT,
		READERROR,
		SENDERROR,
		READMOVE,
		CLIENTMOVE,
		CLIENTERROR,
		SENDENDGAME,
		CLIENTNT,
		SENDPIECE,
		SENDMOVE,
		SERVERMOVE,
		SERVERNT,
		SENDNT;
	}

	private Pile remainingPile;
	private Pile clientHand;
	private Communication comm;
	private Piece startingPiece;
	
	private State STATE = State.PLAYING;
	private Action ACTION = Action.INIT;
	
	/** CURRENT GLOBALS **/
	private String currentErrDescription;
	
	/** CURRENT CLIENT GLOBALS **/
	private Movement currentClientMove;
	private int currentClientHandLength;
	private Piece currentClientSentPiece;
	/** CURRENT SERVER GLOBALS **/
	private Movement currentServerMove;
	
	public ServerDomino(Communication comm){
		super(); // create super resources
		this.comm = comm;
		createDominoResources(); //creamos los recursos del juego


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
		this.clientHand = new Pile(this.remainingPile.getAmount(HANDSIZE));
		
		// obtenemos la pieza que empieza
		this.startingPiece = getStartingPiece();
		
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

	public void initGame(){
		
		while(STATE != State.GAMEENDED){
			
			switch(ACTION){
			
			 // WAIT CASE
			case WAITNEXT: 
				// recibimos la operacion del cliente
				///COMMUNICATION
				Id id = this.comm.readHeader();
				ACTION = convertIdToAction(id);
				break;	
				
			/// INIT CASE
			case INIT:
				// comprobamos quien tiene la primera pieza y entregamos las fichas y el sigiuiente movimiento
				if(this.player.hasPiece(this.startingPiece)){
					//empieza el servidor, enviamos el primer movimiento, con las piezas del cliente
					this.comm.sendInitMovement(clientHand.getPieces(), new Movement(this.startingPiece,null));
					//quitamos la ficha inicial de la mano del servidor
					this.player.removePiece(this.startingPiece);
				}else{
					// Enviamos un movement con ficha null
					this.comm.sendInitMovement(clientHand.getPieces(), new Movement(null,null));
				}
				ACTION = Action.WAITNEXT;
				break;
				
			/// READ CASES	
			case READMOVE:
				///COMMUNICATION
				this.currentClientMove = this.comm.seeClientMovement();
				this.currentClientHandLength = this.comm.seeClientHandLength();
				
				// comprobamos si dice que le quedan 0 fichas
				if(this.currentClientHandLength == 0 && this.clientHand.getLength() > 0){
					this.currentErrDescription = "Numero de fichas pendientes no valido, aun te quedan " + this.clientHand.getLength();
					ACTION = Action.CLIENTERROR;	
				// comprobamos si es un nt
				}else if(this.currentClientMove.isNT()){
					ACTION = Action.CLIENTNT;
				}else{
					ACTION = Action.CLIENTMOVE;
				}
				
				break;
				
				
			case READERROR:	
				 //TODO por ahora
				ACTION = Action.SERVERMOVE;
				break;
				
			//// ACTION DEFINE CASES	
			case CLIENTMOVE:
				// si es una ficha que esta en la mano del cliente y es un movimiento valido
				if(this.clientHand.hasPiece(this.currentClientMove.getPiece()) && 
						this.isValidMovement(this.currentClientMove)){
					//eliminamos la ficha de la mano del cliente, 
					this.clientHand.deletePiece(this.currentClientMove.getPiece());
					//la insertamos en el lado correspondiente de la pila de fichas jugadas
					this.playedPile.pushSide(this.currentClientMove.getPiece(), this.currentClientMove.getSide());
					ACTION = Action.SERVERMOVE;
				}else{
					// especificamos el error
					this.currentErrDescription = "Jugada " + this.currentClientMove.getRepresentation() + " no valida ";
					ACTION = Action.CLIENTERROR;	
				}
				
				break;
				
			case CLIENTNT:
				if (STATE == State.SERVERNT){//venimos de un estado en el que el servidor no podia tirar
					ACTION = Action.SENDENDGAME;
				}else if(remainingPile.getLength() == 0){// si ni hay fichas para robar
					STATE = State.CLIENTNT;
					ACTION = Action.SERVERMOVE;
				}else{
					ACTION = Action.SENDPIECE;
				}
				break;
				
			case CLIENTERROR:
				if(STATE == State.CLIENTERROR){ // si ya venimos de un error previo
					ACTION = Action.SENDENDGAME;
					
				}else{
					ACTION = Action.SENDERROR;
				}
				break;
				
			case SERVERMOVE:

				if(this.player.hasMove(this.playedPile)){// si el servidor puede tirar
					this.currentServerMove = this.player.nextMove(this.playedPile);
					ACTION = Action.SENDMOVE;
				}else{// servidor no puede tirar
					
					// damos fichas al server hasta que pueda tirar o no queden fichas
					while(!this.player.hasMove(this.playedPile) && remainingPile.getLength() > 0){
						this.player.setPiece(this.remainingPile.getRandomPiece());
					}
					
					if(this.player.hasMove(this.playedPile)){ // si al final ha encontrado ficha
						this.currentServerMove = this.player.nextMove(this.playedPile);
						ACTION = Action.SENDMOVE;
					}else{ // despues de robar si puede tirar
						ACTION = Action.SERVERNT;
					}
				}
				break;
				
			case SERVERNT:
				if(STATE == State.CLIENTNT){
					ACTION = Action.SENDENDGAME;
				}else{
					ACTION = Action.SENDNT;
				}
				break;
			
			/////// SEND CASES
			case SENDMOVE:
				sendMovement(this.currentServerMove);
				// el servidor ha ganado
				if(this.player.handLength() == 0){
					ACTION = Action.SENDENDGAME;
				}else{
					STATE = State.PLAYING;
					ACTION = Action.WAITNEXT;
				}
				break;
			
			case SENDNT:
				STATE = State.SERVERNT;
				//montamos el siguiente movimiento que es un NT
				this.currentServerMove = new Movement(null, null);
				//enviamos la jugada con los datos necesarios
				this.comm.sendServerMovement(this.currentServerMove, this.player.handLength(), this.remainingPile.getLength());
				ACTION = Action.WAITNEXT;
				break;
				
			case SENDPIECE:
				this.currentClientSentPiece = this.remainingPile.getRandomPiece(); //extraemos una pieza de la pila de pendientes
				this.clientHand.addPiece(this.currentClientSentPiece);
				sendPieceToClient();
				STATE = State.PLAYING;
				ACTION = Action.WAITNEXT;
				break;
			
			case SENDERROR:
				sendErrorToClient();
				STATE = State.CLIENTERROR;
				ACTION = Action.WAITNEXT;
				break;
			
			case SENDENDGAME: // Aqui no hace falta asiganr accion
				sendEndGameToClient();
				STATE = State.GAMEENDED;
				
				break;
			}
		}//ENDWHILE
	} 
	


	private void closeGame(){
		this.comm.closeConnection();
		this.STATE = State.GAMEENDED;
		
	}
	

	////// SEND FUNCTIONS
	
	private void sendPieceToClient(){
		this.comm.sendPieceToClient(this.currentClientSentPiece);
		
	}
	
	
	private void sendErrorToClient(){
		/*
		 * Informamos la cliente de que ha producido un error
		 */
		//TODO
		
	}
	
	private void sendEndGameToClient() {
		// TODO 
		
	}
	
	/**
	 * Funcion que elimina la ficha de la pila del servidor y la envia cliente
	 * @param move
	 */
	private void sendMovement(Movement move){
		//la eliminamos
		this.player.removePiece(move.getPiece());
		//enviamos la jugada con los datos necesarios
		this.comm.sendServerMovement(move, this.player.handLength(), this.remainingPile.getLength());
	}
	
	
	/////// CONVERTERS
	
	/**
	 * @param id
	 * Funcion que devuelve una accion a realizar a partir de una id
	 * @return
	 */
	private Action convertIdToAction(Id id){
		switch(id){ //parseamos la id recibida con su accion
		
		case TIMEOUT:// si es timeout no pasa nada
			return Action.WAITNEXT; 
				
		case ERROR: // si el cliente nos dice que es error, lo analizamos
			return Action.READERROR;
			
		case ENDGAME: // la dominolayer nos puede indicar endgame debido a algun error IO
			return Action.SENDENDGAME;
			
		case MOVE: // el cliente hace move
			return Action.READMOVE;
			
		default: 
			return Action.SENDENDGAME;
			}
	}
	
	
	private  void testServerDomino(){
		
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
