package collection.threaded;

import java.rmi.RemoteException;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;

public class DiconnectContactThread implements Runnable{
	private Peer2Server peer;
	private String peerName;
	private Peer2Peer contact;
	private String contactName;
	public DiconnectContactThread(Peer2Server peer, String peerName, String contactName){
		System.out.println("Avisando a " +peerName);
		this.peer = peer;
		this.peerName = peerName;
		this.contactName = contactName;
		
	}

	@Override
	public void run() {
		try {
			this.peer.contactExitCallback(contactName);
		} catch (RemoteException e) {
			System.out.println("Unable to connect with " + peerName);
			e.printStackTrace();
		}
		
	}

}
