package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import controllers.GameServer;

public class MainServer {

	private final int TIMEOUT = 5000;
	
	private int port = 8080;
	private boolean serverActive = true;
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainServer main = new MainServer(args);
		
		

	}
	
	public MainServer(String[] args){
		
		ServerSocket mainServerSocket = null;
				
				
		// capturamos el puerto
		if(args.length > 1){
			port = Integer.parseInt(args[0]);
		}
		

		try{
			
			// servidor de sockets
			mainServerSocket = new ServerSocket(port);
			
			System.out.println("Conection open in port " + port);
			while(serverActive){
				
				Socket socket = mainServerSocket.accept();
				socket.setSoTimeout(TIMEOUT);
				socket.setKeepAlive(true);
				System.out.println("Conection accepted from " + socket.getInetAddress());
				
				//iniciamos el nuevo hilo con el GameServer
				new Thread(new GameServer(socket)).start();    
				
			}
			
        } catch (IOException exp) {
            exp.printStackTrace();
        } finally {
            try {
            	mainServerSocket.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }	

	}

}
