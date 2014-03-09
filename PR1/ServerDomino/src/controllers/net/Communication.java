package controllers.net;

import java.io.IOException;
import java.net.Socket;

import net.DominoLayer;

public class Communication extends DominoLayer {

	public Communication(Socket socket) throws IOException {
		super(socket);
	}

	/**
	 * Returns true if the first client message is equal to HELLO
	 * @return
	 */
	public boolean testClient(){
		return Id.HELLO.getVal() == readId();
	}
	
	public boolean sendInit(){
		return writeId(Id.INIT);
	}

}
