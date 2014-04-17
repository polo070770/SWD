package collection.threaded;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;

import java.rmi.RemoteException;

import main.Configuration;

public class DiconnectContactThread implements Runnable {
	private Configuration config;
	private Peer2Server peer;
	private String peerName;
	private Peer2Peer contact;
	private String contactName;

	public DiconnectContactThread(Peer2Server peer, String peerName,
			String contactName) {
		
		this.config = Configuration.getInstance();
		
		this.peer = peer;
		this.peerName = peerName;
		this.contactName = contactName;

	}

	@Override
	public void run() {
		try {
			if(config.DEBUG) System.out.println("Avisando a " + peerName);
			this.peer.contactExitCallback(contactName);
		} catch (RemoteException e) {
			System.out.println("Unable to connect with " + peerName);
			if(config.DEBUG) e.printStackTrace();
		}

	}

}
