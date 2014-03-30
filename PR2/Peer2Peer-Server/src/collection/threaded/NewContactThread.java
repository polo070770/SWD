package collection.threaded;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;

import java.rmi.RemoteException;

public class NewContactThread implements Runnable {
	private Peer2Server peer;
	private String peerName;
	private Peer2Peer contact;
	private String contactName;

	public NewContactThread(Peer2Server peer, String peerName,
			Peer2Peer contact, String contactName) {
		this.peer = peer;
		this.peerName = peerName;
		this.contact = contact;
		this.contactName = contactName;

	}

	@Override
	public void run() {
		try {
			this.peer.newContactCallback(contact, contactName);
		} catch (RemoteException e) {
			System.out.println("Unable to connect with " + peerName);
			e.printStackTrace();
		}

	}

}
