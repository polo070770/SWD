package interficie.peer;

import java.rmi.Remote;

public interface Peer2Server extends Remote  {

	public void newContactCallback(Peer2Peer peer, String peerName) throws java.rmi.RemoteException;;
	public void contactExitCallback(String peerName) throws java.rmi.RemoteException;
	public boolean ping()throws java.rmi.RemoteException;
}
