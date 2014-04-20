package collection.threaded;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.Configuration;
import collection.SyncPeerList;

public class NewGroupThread implements Runnable {
	private Configuration config;
	private Remote peer;
	private String[] contacts;
	private String groupKey;
	private String groupName;

	public NewGroupThread(Remote peer,String groupKey, String groupName, String[] contacts) {
		this.config = Configuration.getInstance();
		this.peer = peer;
		this.contacts = contacts;
		this.groupKey = groupKey;
		this.groupName = groupName;
	}

	@Override
	public void run() {

		try {
			((Peer2Peer) this.peer).getNewGroupCallback(groupKey, groupName, contacts);
		} catch (RemoteException e) {

			System.out.println("Unable to contact peer.");
			if(config.DEBUG)
				e.printStackTrace();
		}

	}

}
