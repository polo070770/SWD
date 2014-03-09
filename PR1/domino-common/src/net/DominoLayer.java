package net;

import java.io.IOException;
import java.net.Socket;


public class DominoLayer {

	
	private ComUtils comm;
	private Socket socket;

	
	public DominoLayer(Socket socket) throws IOException {
		this.socket = socket;
		this.comm = new ComUtils(socket);
		
	};
	
	public void closeConnection(){
		try{
			this.socket.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	
	
	
	/**
	 * Funcion que lee un id
	 * @return
	 */
	
	public int readId(){
		
		try{
			return this.comm.read_int32();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return 0;
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
		HELLO(10), 
		INIT(20),
		MOVE(11),
		PIECE(21),
		ENDGAME(22),
		ERROR(99);
		
		
		private int id;
		private Id(int id){this.id = id;}
		public int getVal(){ return id;}
		
		public static boolean validId(int otherId){
			
			for(Id id : Id.values()){
				if (id.getVal() ==  otherId) return true;
			}
			
			return false;
		}
		
	}
}
