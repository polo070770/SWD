package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import server.ChatServer;

public class MainServer {
	private int port;
	private final boolean DEBUG = true;
	private String serverName;
	private ChatServer server;

	public static void main(String[] args) {
		int port = 1099; // puerto por defecto

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		new MainServer(port);
	}

	public MainServer(int port) {

		this.port = port;

		try {
			LocateRegistry.createRegistry(port); // iniciamos el rmi
			server = new ChatServer(this); // iniciamos el servidor
			String url = "rmi://161.116.52.109:" + port + "/";
			serverName = url + "ChatServer";// registramos el servidor
			Naming.rebind(serverName, server);

			System.out.println("Chat Server iniciado en:");
			System.out.println(Naming.list(url)[0]);

		} catch (Exception e) {
			System.out.println("Imposible iniciar el servidor!");
			if (DEBUG) {
				e.printStackTrace();
			}
		}

	}

	public void CloseServer() {
		try {
			// Desregistramos el servidor
			Naming.unbind(serverName);
			server = null;
		} catch (RemoteException e) {
			System.out.println("Remote Exception, cerrando el servidor!");
			if (DEBUG) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException, cerrando el servidor!");
			if (DEBUG) {
				e.printStackTrace();
			}
		} catch (NotBoundException e) {
			System.out.println("NotBoundException, cerrando el servidor!");
			if (DEBUG) {
				e.printStackTrace();
			}
		}
	}
}
