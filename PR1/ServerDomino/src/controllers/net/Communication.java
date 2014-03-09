package controllers.net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import net.DominoLayer;

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
			Id id = readId();
			if (id == Id.UNKNOWN){
				return false;
			}else if(id == Id.TIMEOUT){
				boolean socketAlive = this.socketAlive();
				if(!socketAlive)return false;
			}
		}
		
	}
	
	public boolean sendInit(){
		return writeId(Id.INIT);
	}

}
