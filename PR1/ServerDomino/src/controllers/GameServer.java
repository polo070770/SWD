package controllers;

import java.net.Socket;

import controllers.net.Communication;

public class GameServer  implements Runnable{

	private Communication comm;
	private boolean connected;
	
	public GameServer(Socket socket){
		
		try{
			this.comm = new Communication(socket);
			connected = true;
		}catch(Exception e){
			connected = false;
			System.out.println(e.toString());
		}
		
	}
	
	
	public boolean isConnected(){
		return this.connected;
	}
	
	public void disconnect(){
		this.connected = false;
		comm.closeConnection();
	}
	
	
	@Override
	public void run() {
		if (connected){
			// aqui hacemos la prueba de si el cliente habla nuestro protocolo, sino
			// nada
			Boolean validClient = comm.testClient();
			if(validClient){
				System.out.println("Valid client, starting Game");
				comm.sendInit();
				ServerDomino game = new ServerDomino(comm);
				
			}else{
				System.out.println("Can not understand, Client refused");
				disconnect();
				
			}
			
		}
		
	}

}
