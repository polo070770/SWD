package peer;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;
import interficie.server.ChatDaemonInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import views.ChatWindow;
import views.MainWindow;
import collection.SyncPeerList;

public class ChatPeer extends UnicastRemoteObject implements Peer2Server,
		Peer2Peer {

	private SyncPeerList contacts;
	private ChatDaemonInterface server;
	private MainWindow window;
	private String peerName;
	private HashMap<String, ChatWindow> chats;
	private ChatWindow newChat;

	public ChatPeer(ChatDaemonInterface server, String nombre)
			throws RemoteException {
		contacts = new SyncPeerList();
		this.server = server;
		this.peerName = nombre;
		this.chats = new HashMap<String, ChatWindow>();

	}

	public void go() {
		window = new MainWindow(this, this.peerName);
		System.out.println(this.peerName + " conectado");
		this.addContacts();
	}

	public void finishConection() {
		try {
			this.server.unregisterPeer(this.peerName);
			System.out.println("Desconectando " + this.peerName);
		} catch (RemoteException e) {
			System.out.println("Error a la hora de finalizar la conexion");
			e.printStackTrace();
		}
	}

	private void addContacts() {
		try {
			Peer2Peer[] newContacts = this.server.getConnectedPeers(peerName);
			if (newContacts.length > 0) {
				contacts.addContacts(newContacts);
				window.addContactsNamesToList(contacts.getContactNames());
			}
		} catch (RemoteException e) {
			System.out.println("Imposible registrar los clientes del servidor");
			e.printStackTrace();
		}
	}

	/**
	 * Funcion que crea un chat a partir del nombre del contacto
	 * 
	 * @param contactName
	 */
	public void newChatWindow(String contactName) {

		if (chats.size() == 0) {

			newChat = new ChatWindow(this, contactName, this.peerName);
			chats.put(contactName, newChat);

		} else {

			if (!newChat.isVisible()) {
				newChat.setVisible();
			}

			if (!chats.containsKey(contactName)) {
				// ChatWindow newChat = new ChatWindow(this, contactName,
				// this.peerName);
				newChat.addConversation(contactName);
				chats.put(contactName, newChat);

			} else {
				newChat.putConversation(contactName);
			}
		}

	}

	public void spreadMessage(String receiver, String message) {
		contacts.spreadMessage(receiver, peerName, message);
	}

	@Override
	public void newMessageCallback(String emisor, String message) {
		newChatWindow(emisor);
		chats.get(emisor).newMessage(message, emisor, false);

	}

	@Override
	public void newContactCallback(Peer2Peer peer, String peerName) {
		// Guardamos el peer en la lista
		if (!(contacts.contains(peerName))) {
			contacts.addPeer(peer, peerName);
			window.addContactNameToList(peerName);
		}
	}

	@Override
	public void contactExitCallback(String peerName) {
		if ((contacts.contains(peerName))) {
			contacts.removePeer(peerName);
			window.removeContactNameFromList(peerName);
		}

	}

	@Override
	public String getNameCallback() throws RemoteException {
		System.out.println("Devuelvo mi nombre " + this.peerName);
		return this.peerName;

	}
}
