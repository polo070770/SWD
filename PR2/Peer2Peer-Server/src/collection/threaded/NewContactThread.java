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
		// peer al que hay que notificar
		this.peer = peer;
		// nombre del peer al que hay que notificar
		this.peerName = peerName;
		
		// peer, nuevo contacto 
		this.contact = contact;
		// nombre del peer, nombre del nuevo contacto
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
