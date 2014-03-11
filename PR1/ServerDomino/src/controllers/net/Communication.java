package controllers.net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import models.Movement;
import models.Piece;
import net.DominoLayer;
import net.DominoLayer.Size;

public class Communication extends DominoLayer {
	

	
	public Communication(Socket socket) throws IOException {
		super(socket);
	}

	/**
	 * Returns true if the first client message is equal to HELLO
	 * @return
	 */
	public boolean waitClientHandshake(){

		while(true){
			Id id = readHeader();
			
			if (id == Id.UNKNOWN){
				return false;
			}else if(id == Id.TIMEOUT){
				boolean socketAlive = this.socketAlive();
				if(!socketAlive){
					return false;
				}
			}else if( id == Id.HELLO){
				return true;
			}
		}
		
	}
	
	public boolean sendInit(){
		return writeId(Id.INIT);
	}
	
	
	
	public void sendInitMovement(Piece[] clientPieces, Movement serverMovement){
		
		char[] initMovement = new char[Size.INIT.asInt()];
		
		int counter = 0;
		// Construimos la parte de piezas del cliente
		for(Piece p: clientPieces){
			initMovement[counter++] = p.getLeft();
			initMovement[counter++] = p.getRight();
		}
		// construimos la parte de piezas del movimiento del server
		for(char serverCharMovement : translateMovement(serverMovement, true)){
			initMovement[counter++] = serverCharMovement;
		}
		
		if(sendHeader(Id.INIT)){
			sendChar(initMovement);
		}
		
		
	}
	
	
	public Movement seeClientMovement(){
		char[] receivedChars = this.recieveChars(Size.MOVEMENT.asInt());
		
		while (receivedChars.length == 0 && this.socketAlive()){
			receivedChars = this.recieveChars(Size.INIT.asInt());
		}
		
		return new Movement(receivedChars);
		
		
	}
	
	public int seeClientHandLength(){
		return this.readInt();
	}
	
	public void sendServerMovement(Movement serverMovement, int hand, int remaining){
		char[] chars = translateMovement(serverMovement);
		if(sendHeader(Id.MOVE)){
			sendChar(chars);
			sendInt(hand);
			sendInt(remaining);
			
		}
	}
	
	public void sendPieceToClient(Piece piece){
		char[] chars = translatePiece(piece);
		
		if(sendHeader(Id.PIECE)){
			sendChar(chars);
		}
	}

}
