package collection;


import java.util.concurrent.ConcurrentHashMap;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import collection.threaded.DiconnectContactThread;
import collection.threaded.NewContactThread;
/**
 * Syncronized Peer2Server list
 * 
 *
 */
public class SyncPeerList {
	private static final int MAX_POOLS = 10;
	ConcurrentHashMap<String, Peer2Server> list;
	
	public SyncPeerList(){
		list = new ConcurrentHashMap<String, Peer2Server>();
		
	}
	
	public SyncPeerList(Peer2Server[] peers){
		this();
		//this.addPeers(peers);
	}
	

	/**
	 * funcion que inserta el peer en la lista
	 * @param peer
	 */
	public void addPeer(Peer2Server peer, String key){
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

	/**
	 * Funcion que notifica a todos los peers que hay un nuevo peer
	 * @param peer
	 * @param name
	 */
	public void spreadNewClient(Peer2Peer peer, String name) {
		
		ExecutorService executor = Executors.newFixedThreadPool(MAX_POOLS);
        for(String key : list.keySet()){
        	if(key != name){
        		// evitamos informar al propio peer de su conexion
        		Runnable worker = new NewContactThread(list.get(key), key, peer, name);
        		executor.execute(worker);
        	}
        }
        executor.shutdown();
        //while (!executor.isTerminated());
	}
	
	public void spreadDisconnectClient(String name){
		ExecutorService executor = Executors.newFixedThreadPool(MAX_POOLS);
        for(String key : list.keySet()){
    		// avisamos al resto de peers de la desconexion
    		Runnable worker = new DiconnectContactThread(list.get(key), key, name);
    		executor.execute(worker);
        }
        executor.shutdown();
        //while (!executor.isTerminated());
	}
	
	
	public Peer2Peer[] getConnectedPeers(String peerName){
		// si solo hay un cliente, es el que solicita los nombres
		if (list.size() == 1){
			return new Peer2Peer[0];
		}
		
		Peer2Peer[] tempList = new Peer2Peer[list.size()-1];
		int i = 0;
		synchronized(list){
			for(String key: list.keySet()){
				if(!key.equalsIgnoreCase(peerName)){
					tempList[i++] = (Peer2Peer)list.get(key);
					
				}
			}
		}
		return tempList;
	}
}
