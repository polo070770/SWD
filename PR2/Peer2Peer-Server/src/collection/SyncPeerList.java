package collection;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.Configuration;
import collection.threaded.DiconnectContactThread;
import collection.threaded.NewContactThread;
import collection.threaded.PeerCleaner;

/**
 * Syncronized Peer2Server list
 * 
 */
public class SyncPeerList {
	private Configuration config;

	
	ConcurrentHashMap<String, Peer2Server> list;

	public SyncPeerList() {
		config = Configuration.getInstance();
		list = new ConcurrentHashMap<String, Peer2Server>();
		
		
	}

	public SyncPeerList(Peer2Server[] peers) {
		this();
		// this.addPeers(peers);
	}

	/**
	 * Funcion que inserta el peer en la lista
	 * 
	 * @param peer
	 */
	public boolean addPeer(Peer2Server peer, String key) {
		// anadimos el peer en synchro mode
		synchronized (list) {
			if (!list.containsKey(key)){
					list.put(key, peer);
					return true;
			}
			return false;
		}
	}

	/**
	 * Funcion que elimina el peer de la lista
	 * 
	 * @param peer
	 */
	public boolean removePeer(String key) {
		// anadimos el peer en synchro mode
		synchronized (list) {
			if (list.containsKey(key)){
				list.remove(key);
				return true;
			}
			return false;
			
		}
	}

	public void showPeers() {

	}

	/**
	 * Funcion que devuelve si la lista contiene un peer
	 * 
	 * @param peer
	 * @return
	 */
	public boolean contains(String key) {
		return list.containsKey(key);
	}

	/**
	 * Funcion que devuelve el numero de peers conectados
	 * 
	 * @return
	 */
	public int numPeersConnected() {
		return this.list.size();
	}

	/**
	 * Funcion que notifica a todos los peers que hay un nuevo peer
	 * 
	 * @param peer
	 * @param name
	 */
	public void spreadNewClient(Peer2Peer peer, String name) {

		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_NEW_CLIENT);
		for (String key : list.keySet()) {
			if (key != name) {
				// evitamos informar al propio peer de su conexion
				Runnable worker = new NewContactThread(list.get(key), key,
						peer, name);
				executor.execute(worker);
			}
		}
		executor.shutdown();
	}

	/**
	 * Funcion que notifica a todos los peers que se ha desconecteado un peer
	 * 
	 * @param name
	 */
	public void spreadDisconnectClient(String name) {
		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_CLIENT_DELETED);
		for (String key : list.keySet()) {
			// avisamos al resto de peers de la desconexion
			Runnable worker = new DiconnectContactThread(list.get(key), key,
					name);
			executor.execute(worker);
		}
		executor.shutdown();
	}

	/**
	 * Funcion que retorna todos los peers conectados
	 * 
	 * @param peerName
	 * @return
	 */
	public Peer2Peer[] getConnectedPeers(String peerName) {
		// si solo hay un cliente, es el que solicita los nombres
		if (list.size() == 1) {
			return new Peer2Peer[0];
		}

		Peer2Peer[] tempList = new Peer2Peer[list.size() - 1];
		int i = 0;
		synchronized (list) {
			for (String key : list.keySet()) {
				if (!key.equalsIgnoreCase(peerName)) {
					tempList[i++] = (Peer2Peer) list.get(key);

				}
			}
		}
		return tempList;
	}
	

	/**
	 * Funcion que elimina un peer de la lista e informa al resto de que ese peer ya no esta conectado
	 * @param key
	 */
	public void removeAndSpread(String key){
		if(this.removePeer(key)){
			this.spreadDisconnectClient(key);
		}
		
	}
	
	/**
	 * Funcion que devuelve una copia de los peers Conectados
	 * @return
	 */
	public ConcurrentHashMap<String, Peer2Server> getPeersCopy(){
		synchronized (this.list) {
			ConcurrentHashMap<String, Peer2Server> list = new ConcurrentHashMap<String, Peer2Server>();
			for (String key : list.keySet()) {
				list.put(key, this.list.get(key));
			}
		}
		return list;
		
	}
}
