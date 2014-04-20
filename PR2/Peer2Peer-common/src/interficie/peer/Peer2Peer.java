package interficie.peer;

import java.rmi.Remote;

public interface Peer2Peer extends Remote  {

	public void newMessageCallback(String emisor, String message) throws java.rmi.RemoteException;
	public String getNameCallback() throws java.rmi.RemoteException;
	public boolean ping()throws java.rmi.RemoteException;
	public void getNewGroupCallback(String newGroupId, String NewGroupName, String[] newContactsKey)throws java.rmi.RemoteException;
	public void getNewMessageGroupCallback(String emisor, String groupId, String newMessage)throws java.rmi.RemoteException;
	public void getNewClientGroupCallback(String newContactKey, String groupId) throws java.rmi.RemoteException;
	public void getNewClientGroupCallback(String[] newContactsKeys, String groupId) throws java.rmi.RemoteException;
	public void getDeleteClientGroupCallback(String contactKey, String groupId)throws java.rmi.RemoteException;
	
	
}
