package net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import models.Movement;
import models.Piece;
import models.DomError;

public class DominoLayer {

	public enum Id {

		UNKNOWN(-2), 
		TIMEOUT(-1), 
		HELLO(10), 
		INIT(20), 
		INITSERVER(20),
		MOVE(11), 
		MOVESERVER(21),
		/*, PIECE(21) no se hace distincion*/
		ENDGAME(22), 
		ERROR(99);

		public static Id fromInt(int num) {
			for (Id id : Id.values()) {
				if (id.getVal() == num)
					return id;
			}
			return UNKNOWN;
		}

		public static boolean validId(int otherId) {

			for (Id id : Id.values()) {
				if (id.getVal() == otherId)
					return true;
			}

			return false;
		}

		private int id;

		private Id(int id) {
			this.id = id;
		}

		public int getVal() {
			return id;
		}

	}

	public enum Size {
		INIT(18), 
		INITSERVER(18),
		MOVEMENT(4), 
		PIECE(4), 
		INITPIECE(2), 
		ERRORLENGTHDESC(3);
		private int size;

		private Size(int length) {
			this.size = length;
		}

		public int asInt() {
			return size;
		}

	}
	protected final boolean LOG = true;
	private ComUtils comm;
	protected Socket socket;

	public DominoLayer(Socket socket) throws IOException {
		this.socket = socket;
		this.comm = new ComUtils(socket);

	}

	/**
	 * Function that closes the current conection
	 * 
	 * @return
	 */
	public boolean closeConnection() {
		boolean closed = false;

		try {
			this.socket.close();
			closed = true;
		} catch (IOException e) {
			e.printStackTrace();
			return closed;
		} catch (Exception e) {
			e.printStackTrace();
			return closed;
		}
		return closed;
	}

	/**
	 * Funcion que lee un id
	 * 
	 * @return
	 */

	@SuppressWarnings("finally")
	private Id readId() {

		try {
			return Id.fromInt(this.comm.read_int32());
		} catch (SocketTimeoutException e) {
			if(LOG){
			System.out.println(this.socket.getInetAddress() + ":"
					+ this.socket.getPort() + " TIMEOUT");
			}
			return Id.TIMEOUT;
		} catch (SocketException e) {
			e.printStackTrace();
			return Id.ENDGAME;
		} catch (IOException e) {
			e.printStackTrace();
			return Id.ENDGAME;
		}

	}

	protected int readInt() {
		try {
			return this.comm.read_int32();
		} catch (SocketTimeoutException e) {
			if(LOG){
			System.out.println(this.socket.getInetAddress() + ":"
					+ this.socket.getPort() + " TIMEOUT");
			}
		} catch (SocketException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
		return 0;
	}

	/**
	 * Returns if socket is alive or not
	 * 
	 * @return
	 */
	public boolean socketAlive() {
		try {
			boolean alive = this.socket.getKeepAlive();
			if (alive) {
				System.out.println(this.socket.getInetAddress() + ":"
						+ this.socket.getPort() + " ALIVE");
				return true;
			} else {
				System.out.println(this.socket.getInetAddress() + ":"
						+ this.socket.getPort() + "NOT ALIVE");
				return false;
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Writes an id trough the socket and returns true if it was succesful
	 * 
	 * @param id
	 * @return
	 */
	protected boolean writeId(Id id) {
		try {
			this.comm.write_int32(id.getVal());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected boolean sendHeader(Id id) {

		try {
			this.comm.write_int32(id.getVal());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Id readHeader() {
		Id id;
		while ((id = readId()) == Id.TIMEOUT && socketAlive())
			;
		if (!socketAlive())
			return Id.ENDGAME;

		return id;

	}

	public void sendInt(int value) {
		try {
			this.comm.write_int32(value);
		} catch (IOException e) {
			System.out.println("Error sending integer");
			e.printStackTrace();
		}
	}

	/**
	 * Send array of chars and return true if it is successful
	 * 
	 * @param chars
	 * @return
	 */
	protected boolean sendChar(char[] chars) {
		try {
			this.comm.write_char(chars);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Funcion que devuelve un array de chars recibidos, devuelve un array vacio
	 * si hay error
	 * 
	 * @param size
	 * @return
	 */

	protected char[] recieveChars(int size) {
		char[] recieved = new char[size];
		try {
			recieved = this.comm.read_char(size);
			return recieved;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return new char[0];

		} catch (IOException e) {
			e.printStackTrace();
			return new char[0];
		}

	}

	/**
	 * convierte un movimiento al protocolo domino
	 * 
	 * @param move
	 * @param initMovement
	 *            , especifica si es el movimiento inicial que no tiene en
	 *            cuenta si la ficha esta girada o el lado de la mesa
	 * @return
	 */
	protected char[] translateMovement(Movement move, boolean initMovement) {
		char[] newMovement = new char[Size.MOVEMENT.asInt()];

		if (move.getPiece() == null) {
			newMovement[0] = 'N';
			newMovement[1] = 'T';
			newMovement[2] = '0';
			newMovement[3] = '0';
		} else {
			Piece piece = move.getPiece();
			newMovement[0] = piece.getLeft();
			newMovement[1] = piece.getRight();

			if (initMovement) {
				newMovement[2] = '0';
				newMovement[3] = '0';
			} else {
				newMovement[2] = piece.reversed() ? '1' : '0';
				newMovement[3] = move.getSide().asChar();
			}

		}
		return newMovement;
	}

	/**
	 * convierte un movimiento al protocolo domino
	 * 
	 * @param move
	 * @return
	 */
	protected char[] translateMovement(Movement move) {
		return translateMovement(move, false);
	}

	
	
	protected char[] translatePiece(Piece piece) {
		char[] chars = new char[Size.PIECE.asInt()];
		chars[0] = piece.getLeft();
		chars[1] = piece.getRight();
		chars[2] = piece.reversed() ? '1' : '0';
		chars[3] = '0'; //padding

		return chars;
	}
	/**
	 * Funcion que recibe un estring de error y devuelve un array de chars conteniendo
	 * la longitud del string en los tres primeros chars y el error parseado a chars
	 * @param errDescrtpion
	 * @return
	 */
	protected char[] translateErrorDescription(String errDescription){
		char [] chars;
		String errLength = "00" + errDescription.length();
		//parseamos el error a tres digitos
		errLength = errLength.substring(errLength.length() - Size.ERRORLENGTHDESC.asInt() - 1 , errLength.length() -1);
	
		chars = (errLength + errDescription).toCharArray();
		return chars;
	}

	
	public DomError seeError(){
		int errNum = this.readInt();
		char[] errorLengthDesc = this.recieveChars(Size.ERRORLENGTHDESC.asInt());  
		char[] errorDesc = this.recieveChars(Integer.parseInt(String.valueOf(errorLengthDesc)));  
		return new DomError(errNum, errorDesc);
		
	}
	
	public void sendError(DomError err){
		
		char[] chars = translateErrorDescription(err.getDesc());
		if(sendHeader(Id.ERROR)){
			sendInt(err.getErrNum());
			sendChar(chars);
		}
		
	}
	
}
