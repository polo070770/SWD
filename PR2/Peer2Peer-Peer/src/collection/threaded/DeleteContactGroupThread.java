package collection.threaded;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Configuration;
import collection.SyncPeerList;

public class DeleteContactGroupThread implements Runnable {
	private Configuration config;
	private Remote targetPeer;
	private String contactName;
	private String groupId;

	public DeleteContactGroupThread(Remote targetPeer,String contactName, String groupId) {
		this.config = Configuration.getInstance();
		this.targetPeer = targetPeer;
		this.contactName = contactName;
		this.groupId = groupId;
	}

	@Override
	public void run() {

		try {
			((Peer2Peer) this.targetPeer).getDeleteClientGroupCallback(contactName,groupId);
		} catch (RemoteException e) {

			System.out.println("Unable to contact peer.");
			if(config.DEBUG)
				e.printStackTrace();
		}

	}

}
