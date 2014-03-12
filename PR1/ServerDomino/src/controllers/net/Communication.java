package controllers.net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import models.Movement;
import models.Piece;
import net.DominoLayer;
import net.DominoLayer.Size;
import models.DomError;

public class Communication extends DominoLayer {
	

	private String socket_desc;
	public Communication(Socket socket) throws IOException {
		super(socket);
		this.socket_desc = socket.getInetAddress() + ":" + socket.getPort();
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
		return writeId(Id.INITSERVER);
	}
	
	
	
	public void sendInitMovement(Piece[] clientPieces, Movement serverMovement){
		
		char[] initMovement = new char[Size.INITSERVER.asInt()];
		
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
		
		if(sendHeader(Id.INITSERVER)){
			sendChar(initMovement);
		}
		
		
	}
	
	/// SERVER see from client functions
	/**
	 * 
	 * @return
	 */
	public Movement seeClientMovement(){
		char[] receivedChars = this.recieveChars(Size.MOVEMENT.asInt());
		return new Movement(receivedChars);
		
	}
	
	
	
	public int seeClientHandLength(){
		return this.readInt();
	}
	
	public void sendServerMovement(Movement serverMovement, int hand, int remaining){
		char[] chars = translateMovement(serverMovement);
		if(sendHeader(Id.MOVESERVER)){
			sendChar(chars);
			sendInt(hand);
			sendInt(remaining);
			
		}
	}
	
	public void sendPieceToClient(Piece piece){
		char[] chars = translatePiece(piece);
		
		if(sendHeader(Id.MOVESERVER)){
			sendChar(chars);
		}
	}
	
	public void sendErrorToClient(DomError err){
		this.sendError(err);
	}
	
	public void sendEndGameToClient(int clientHand, int serverHand){
		if(sendHeader(Id.ENDGAME)){
			sendInt(clientHand);
			sendInt(serverHand);
		}
	}
	
	public String getScocketDescription(){
		return this.socket_desc;
	}

}
