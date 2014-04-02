package collection;



import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Syncronized Peer2Server list
 * 
 *
 */
public class SyncPeerList {
	private static final int MAX_POOLS = 10;
	private ConcurrentHashMap<String, Peer2Peer> list;
	
	public SyncPeerList(){
		list = new ConcurrentHashMap<String, Peer2Peer>();
	}
	
	public SyncPeerList(Peer2Peer[] peers){
		this();
		//this.addPeers(peers);
	}
	
	/**
	 * funcion que inserta el peer en la lista
	 * @param peer
	 */
	public void addPeer(Peer2Peer peer, String key){
		// anadimos el peer en synchro mode
		synchronized(list){
			if (!list.containsKey(key))
				list.put(key,peer);
		}
	}
	/**
	 * funcion que elimina  el peer de la lista
	 * @param peer
	 */
	public void removePeer(String key){
		// anadimos el peer en synchro mode
		synchronized(list){
			if (list.containsKey(key))
				list.remove(key);
		}
	}
	
	public void showPeers(){
		
	}
	/**
	 * Funcion que devuelve si la lista contiene un peer
	 * @param peer
	 * @return
	 */
	public boolean contains(String key){
		return list.containsKey(key);
	}
	
	public int numPeersConnected(){
		return this.list.size();
	}
	
	public String[] getContactNames(){
		String[] contacts = new String[list.size()];
		int i = 0;
		for(String key : list.keySet()){
			contacts[i++] = key;
		}
		return contacts;
	}
	
	public void addContacts(Remote[] contacts) throws RemoteException{
		for(int i = 0; i < contacts.length; i++ ){
			
			Peer2Peer peer = (Peer2Peer) contacts[i];
			
			String key = peer.getNameCallback();
			
			System.out.println("Recibo nombre " + key);
			addPeer(peer, key);
			
		}

	}
	
	public void spreadMessage(String receiver, String emisor, String message){
		synchronized(list){
			if (list.containsKey(receiver))
				try {
					list.get(receiver).newMessageCallback(emisor, message);
				} catch (RemoteException e) {
					System.out.println("Imposible contactar con " + receiver);
					e.printStackTrace();
				}
		}
	}
}
