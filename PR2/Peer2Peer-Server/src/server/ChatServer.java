package server;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;
import interficie.server.ChatDaemonInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.Configuration;
import main.MainServer;
import views.MainWindow;
import collection.SyncPeerList;
import collection.threaded.PeerCleaner;

public class ChatServer extends UnicastRemoteObject implements
		ChatDaemonInterface {

	private SyncPeerList peers;
	private MainWindow window;
	private MainServer context;
	private PeerCleaner cleaner;
	private Configuration config;

	public ChatServer(MainServer context) throws RemoteException {
		this.config = Configuration.getInstance();
		this.context = context;
		// lista de peers
		peers = new SyncPeerList();
		// ventana per al server
		window = new MainWindow(this);
		this.startCleaner(); 
	}

	/**
	 * Cierra el servidor.
	 */
	public void stopServer() {
		System.out.println("Closing server BYE!");
		this.stopCleaner();
		this.context.CloseServer();
	}

	@Override
	public boolean registerPeer(String name, Peer2Server peer)
			throws RemoteException {
		boolean result = false;
		// Guardamos el peer en la lista
		if (peers.addPeer(peer, name)) {
			System.out.println("Registrado " + name);
			window.addPeerNameToList(name);
			// informamos al resto de los peers, pasamos la referencia como un
			// peer2peer y no un peer2server
			peers.spreadNewClient((Peer2Peer) peer, name);
			result = true;
		}
		System.out.println("Peers conectados : " + peers.numPeersConnected());
		return result;
	}

	@Override
	public void unregisterPeer(String name) throws RemoteException {
		// Eliminamos el elemento de la lista
		System.out.println("Solicitud de eliminacion " + name);
		this.deletePeer(name);
		System.out.println("Peers conectados : " + peers.numPeersConnected());
	}

	@Override
	public Peer2Peer[] getConnectedPeers(String peerName)
			throws RemoteException {
		return peers.getConnectedPeers(peerName);
	}
	/**
	 * Funcion que activa o detiene el peerCleaner
	 * @param action true:activa, false:detiene
	 */
	public void startStopPeerCleaner(boolean action){
		if(action){
			this.startCleaner();
		}else{
			this.stopCleaner();
		}
	}
	/**
	 * funcion que para el peerCleaner
	 */
	public void stopCleaner(){
		cleaner.disconnect();
	}
	
	/**
	 * Funcio que inicia el peerCleaner
	 */
	public void startCleaner(){
		cleaner = new PeerCleaner(peers, this);
		new Thread(cleaner).start();
	}
	
	/**
	 * Funcion que elimina un peer de la lista de peers e informa al resto de peers (solo si existe el peer) 
	 * @param name
	 */
	public void deletePeer(String name){
		if (peers.removePeer(name)) {
			window.removePeerNameFromList(name);
			// informamos al resto de los peers que ha habido una desconexion
			peers.spreadDisconnectClient(name);
		}
	}
}
