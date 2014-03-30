package main;

import interficie.server.ChatDaemonInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import peer.ChatPeer;
import views.DialogWindow;

public class MainPeer {
	private static final boolean DEBUG = true;
	static final String serverName = "ChatServer";
	private int port;
	private String host;
	private String nombre;
	private ChatPeer client;

	public static void main(String[] args) {
		int port = 1099; // puerto por defecto
		String host = "localhost"; // host por defecto
		String nombre = "Hermetico"; // nombre por defecto
		
		//Nombre 
		if(args.length > 2){
			nombre = args[2];
		}
		
		// puerto 
		if(args.length > 1){
			port = Integer.parseInt(args[1]);
		}
		
		//host 
		if(args.length > 0){
			host = args[0];
		}
		
		
		MainPeer chat = new MainPeer(host, port, nombre);

	}

	public MainPeer(String host, int port, String nombre) {
		this.host = host;
		this.port = port;
		this.nombre = nombre;

		String urlRegistro = "rmi://" + host + ":" + port + "/" + serverName;
		try {
			ChatDaemonInterface chatServer = (ChatDaemonInterface) Naming
					.lookup(urlRegistro);
			System.out.println("Server encontrado");

			client = new ChatPeer(chatServer, nombre);
			chatServer.registerPeer(nombre, client);
			client.go();

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Imposible conectar al servidor");
			if (DEBUG)
				e.printStackTrace();
		}

	}

}
