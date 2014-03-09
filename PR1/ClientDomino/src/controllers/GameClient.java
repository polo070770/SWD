package controllers;

import java.net.Socket;

import controllers.net.Communication;

public class GameClient {
	
	private Communication comm;
	

	public GameClient(Socket socket){
		
		try{
			this.comm = new Communication(socket);
			this.comm.testServer();
			System.out.println("Connected to server!");
			System.out.println("Received id " + comm.readId());
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
}
