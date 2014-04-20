package collection.threaded;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Configuration;
import collection.SyncPeerList;

public class NewClientGroupThread implements Runnable {
	private Configuration config;
	private Remote targetPeer;
	private String[] newContactsName;
	private String groupId;

	public NewClientGroupThread(Remote targetPeer,String[] newContactsName, String groupId) {
		this.config = Configuration.getInstance();
		this.targetPeer = targetPeer;
		this.newContactsName = newContactsName;
		this.groupId = groupId;
	}

	@Override
	public void run() {

		try {
			((Peer2Peer) this.targetPeer).getNewClientGroupCallback(newContactsName, groupId);
		} catch (RemoteException e) {

			System.out.println("Unable to contact peer.");
			if(config.DEBUG)
				e.printStackTrace();
		}

	}

}
