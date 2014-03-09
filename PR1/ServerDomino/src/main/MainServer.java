package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import controllers.GameServer;

public class MainServer {

	private int port = 8080;
	private int MAXCLIENTS = 500;
	private ArrayList<Thread> threads;
	private int currentServers = 0;
	private boolean serverActive = true;
	private ServerSocket mainServerSocket;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainServer main = new MainServer(args);
		
		

	}
	
	public MainServer(String[] args){
		
		// capturamos el puerto
		if(args.length > 1){
			port = Integer.parseInt(args[0]);
		}
		try{
			
			// servidor de sockets
			mainServerSocket = new ServerSocket(port);
			System.out.println("Conection open in port " + port);
			
			ClientThreader clientThreader = new ClientThreader();
			Thread thread = new Thread(clientThreader);
			thread.start();
			
            while(serverActive);
            
            System.out.println("Closing server");
            clientThreader = null;
            
        }catch(Exception e){
        	System.out.println(e.getMessage());
        } 		

	}
	


	private class ClientThreader implements Runnable{
	
		@Override
		public void run() {
			
			try{
				while(serverActive){
		            Socket socket = mainServerSocket.accept();
		            System.out.println("Conection accepted from " + socket.getInetAddress());
		            
		            //creamos el nuevo gameServer
		            GameServer gameServer = new GameServer(socket);
		            Thread gameThread = new Thread(gameServer);
		            gameThread.start();
		            threads.add(gameThread);
				}
				
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
	}

}
