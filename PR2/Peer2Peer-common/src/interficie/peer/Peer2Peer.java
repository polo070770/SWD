package interficie.peer;

import java.rmi.Remote;

public interface Peer2Peer extends Remote  {

	public void newMessageCallback(String emisor, String message) throws java.rmi.RemoteException;
	public String getNameCallback() throws java.rmi.RemoteException;
	public boolean ping()throws java.rmi.RemoteException;
}
