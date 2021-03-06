package collection.threaded;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Configuration;
import collection.SyncPeerList;

public class AddContactThread implements Runnable {
	private Configuration config;
	private SyncPeerList context;
	private Peer2Peer peer;
	private String key;

	public AddContactThread(SyncPeerList context, Remote remote) {
		this.config = Configuration.getInstance();
		this.peer = (Peer2Peer) remote;
		this.context = context;
		this.key = null;
	}

	@Override
	public void run() {

		try {

			this.key = peer.getNameCallback();

		} catch (RemoteException e) {
			System.out.println("Unable to get the name of a peer.");
			if(config.DEBUG)
			e.printStackTrace();
		} finally {
			if (key != null) {
				System.out.println("Recibo nombre " + key);
				context.addPeer(peer, key);
			}
		}

	}

}
