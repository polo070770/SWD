package collection.threaded;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Configuration;
import collection.SyncPeerList;

public class NewMessageGroupThread implements Runnable {
	private Configuration config;
	private Remote targetPeer;
	private String groupKey;
	private String emisor;
	private String message;

	public NewMessageGroupThread(Remote targetPeer,String emisor, String groupKey, String message) {
		this.config = Configuration.getInstance();
		this.targetPeer = targetPeer;
		this.message = message;
		this.groupKey = groupKey;
		this.emisor = emisor;
	}

	@Override
	public void run() {

		try {
			((Peer2Peer) this.targetPeer).getNewMessageGroupCallback(emisor, groupKey, message);

		} catch (RemoteException e) {

			System.out.println("Unable to contact peer.");
			if(config.DEBUG)
				e.printStackTrace();
		}

	}

}
