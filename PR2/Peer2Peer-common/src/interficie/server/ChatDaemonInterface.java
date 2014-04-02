package interficie.server;

import interficie.peer.Peer2Server;

import java.rmi.Remote;

public interface ChatDaemonInterface extends Remote {
	
	/**
	 * Funcion que registra un peer
	 * @param peer
	 * @throws java.rmi.RemoteException
	 */
	public boolean registerPeer(String name, Peer2Server peer) throws java.rmi.RemoteException;
	
	/**
	 * Funcion que elimina un peer del registro
	 * @param peer
	 * @throws java.rmi.RemoteException
	 */
	public void unregisterPeer(String name) throws java.rmi.RemoteException;
	
	public Remote[] getConnectedPeers(String peerName) throws java.rmi.RemoteException;

}
