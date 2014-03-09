package net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class DominoLayer {

	
	private ComUtils comm;
	protected Socket socket;

	
	public DominoLayer(Socket socket) throws IOException {
		this.socket = socket;
		this.comm = new ComUtils(socket);
		
	};
	
	
	/**
	 * Function that closes the current conection
	 * @return
	 */
	public boolean closeConnection(){
		boolean closed = false;
		
		try{
			this.socket.close();
			closed = true;
		}catch(IOException e){
			e.printStackTrace();
			return closed;
		}catch(Exception e){
			e.printStackTrace();
			return closed;
		}
		return closed;
	}

	
	/**
	 * Returns if socket is alive or not
	 * @return
	 */
	public boolean socketAlive(){
		try {
			boolean alive = this.socket.getKeepAlive();
			if (alive){
				System.out.println(this.socket.getInetAddress() + ":" + this.socket.getPort() + " ALIVE");
				return true;
			}else{
				return false;
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	/**
	 * Funcion que lee un id
	 * @return
	 */
	
	@SuppressWarnings("finally")
	public Id readId(){
		
		try{
			return Id.fromInt(this.comm.read_int32());
		}catch(SocketTimeoutException e){
			System.out.println(this.socket.getInetAddress() + ":" + this.socket.getPort() + " TIMEOUT");
			return Id.TIMEOUT;
		}catch(IOException e){
			System.out.println(e.getMessage());
		}

		return Id.UNKNOWN;
		
	}
	
	/**
	 * Writes an id trough the socket and returns true if it was succesful
	 * @param id
	 * @return
	 */
	public boolean writeId(Id id){
		try{
			this.comm.write_int32(id.getVal());
			return true;
		}catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	

	
	public enum Id{
		UNKNOWN(-2),
		TIMEOUT(-1),
		HELLO(10), 
		INIT(20),
		MOVE(11),
		PIECE(21),
		ENDGAME(22),
		ERROR(99);
		
		
		private int id;
		private Id(int id){this.id = id;}
		public int getVal(){ return id;}
		
		public static Id fromInt(int num){
			for(Id id : Id.values()){
				if (id.getVal() ==  num) return id;
			}
			return UNKNOWN;
		}
		public static boolean validId(int otherId){
			
			for(Id id : Id.values()){
				if (id.getVal() ==  otherId) return true;
			}
			
			return false;
		}
		
	}
}
