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
	public boolean requestHandShake(){
		// el mensaje de handhake o sincronizacion es un Hello
		return writeId(Id.HELLO);
	}
	

}