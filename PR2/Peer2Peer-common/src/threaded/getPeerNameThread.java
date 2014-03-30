package threaded;

import java.rmi.RemoteException;

import interficie.peer.Peer2Peer;

public class getPeerNameThread implements Runnable{
	private Peer2Peer contact;
	
	public getPeerNameThread( Peer2Peer contact){
		this.contact = contact;
		
	}

	@Override
	public void run() {
		try {
			this.contact.getNameCallback();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
