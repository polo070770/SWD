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

		// puerto
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}

		// host
		if (args.length > 0) {
			host = args[0];
		}

		new MainPeer(host, port);

	}

	public MainPeer(String host, int port) {

		this.host = host;
		this.port = port;

		String urlRegistro = "rmi://" + host + ":" + port + "/" + serverName;

		userNameDialog(urlRegistro, false);

		try {
			ChatDaemonInterface chatServer = (ChatDaemonInterface) Naming
					.lookup(urlRegistro);
			System.out.println("Server encontrado");

			client = new ChatPeer(chatServer, this.nombre);

			while (!chatServer.registerPeer(this.nombre, client)) {
				userNameDialog(urlRegistro, true);
				client = new ChatPeer(chatServer, this.nombre);
			}

			client.go();

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Imposible conectar al servidor");
			if (DEBUG)
				e.printStackTrace();
		}

	}

	private void userNameDialog(String url, boolean repeat) {
		// pop-up para preguntar el username
		DialogWindow dialog = new DialogWindow(this, url);
		if (repeat)
			dialog.setLblUserNameAlreadyIn(true);

		Thread thr = new Thread(dialog);
		thr.start();

		synchronized (this) {
			while (!dialog.isReady()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					System.out
							.println("InterruptedException, user name dialog!");
					if (DEBUG)
						e.printStackTrace();
				}
			}
		}

		// Nos quedamos con el username insertado por el cliente
		this.nombre = dialog.getUserName();

		dialog.dispose();
		// Si se ha dado al boton de cancelar se cierra todo el programa cliente
		if (this.nombre == null)
			System.exit(0);

	}
}
