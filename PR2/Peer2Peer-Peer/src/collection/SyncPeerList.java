package collection;

import interficie.peer.Peer2Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.Configuration;
import collection.threaded.AddContactThread;
import collection.threaded.DeleteContactGroupThread;
import collection.threaded.NewClientGroupThread;
import collection.threaded.NewMessageGroupThread;
import collection.threaded.NewGroupThread;

/**
 * Syncronized Peer2Server list
 * 
 * 
 */
public class SyncPeerList {
	private static final int MAX_POOLS = 10;
	private ConcurrentHashMap<String, Peer2Peer> list;
	private Configuration config;

	public SyncPeerList() {
		config = Configuration.getInstance();
		list = new ConcurrentHashMap<String, Peer2Peer>();
	}

	public SyncPeerList(Peer2Peer[] peers) {
		this();
		// this.addPeers(peers);
	}

	/**
	 * funcion que inserta el peer en la lista
	 * 
	 * @param peer
	 */
	public void addPeer(Peer2Peer peer, String key) {
		// anadimos el peer en synchro mode
		synchronized (list) {
			if (!list.containsKey(key))
				list.put(key, peer);
		}
	}

	/**
	 * funcion que elimina el peer de la lista
	 * 
	 * @param peer
	 */
	public void removePeer(String key) {
		// anadimos el peer en synchro mode
		synchronized (list) {
			if (list.containsKey(key))
				list.remove(key);
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

	public int numPeersConnected() {
		return this.list.size();
	}

	public String[] getContactNames() {
		String[] contacts = new String[list.size()];
		int i = 0;
		for (String key : list.keySet()) {
			contacts[i++] = key;
		}
		return contacts;
	}

	public void addContacts(Remote[] contacts) throws RemoteException {

		ExecutorService executor = Executors.newFixedThreadPool(MAX_POOLS);

		for (int i = 0; i < contacts.length; i++) {

			Runnable worker = new AddContactThread(this, contacts[i]);
			executor.execute(worker);

		}

		executor.shutdown();
		// ponemos esto por que inmediatamente despues añadimos los contactos a
		// la ventana del chat y claro, como son hilos diferentes
		// no tienen por que estar ya los nombres en la lista
		while (!executor.isTerminated()) {
				try {
					//esperamos a que respondan todos
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println("Excepcion a la hora de añadir contactos");
					if(config.DEBUG)
					e.printStackTrace();
				}
			
        }

	}

	public void spreadMessage(String receiver, String emisor, String message) {
		synchronized (list) {
			if (list.containsKey(receiver))
				try {
					list.get(receiver).newMessageCallback(emisor, message);
				} catch (RemoteException e) {
					System.out.println("Imposible contactar con " + receiver);
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Funcion que informa a los contactos de que se les ha incluido en un grupo
	 * @param groupKey
	 * @param groupName
	 * @param contacts
	 */
	public void spreadNewGroup(String groupKey, String groupName, String[] recipients, String[] contacts){
		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_NEW_GROUP);
		for(String name : recipients){
			Remote peer = list.get(name);
			Runnable worker = new NewGroupThread(peer, groupKey, groupName, contacts);
			executor.execute(worker);
		}
		executor.shutdown();
		
	}
	/**
	 * funcion que emite un nuevo mensaje a todos los usuarios del grupo
	 * @param emisor
	 * @param groupKey
	 * @param message
	 * @param contacts
	 */
	public void spreadGroupMessage(String emisor, String groupKey, String message, String[] contacts){
		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_NEW_MESSAGE_GROUP);
		for(String name : contacts){
			Remote peer = list.get(name);
			Runnable worker = new NewMessageGroupThread(peer, emisor, groupKey, message);
			executor.execute(worker);
		}
		executor.shutdown();
		
	}
	public void spreadDeleteClientGroup(String emisor, String groupKey, String[] recipients){
		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_DISCONNECT_CLIENT_GROUP);
		for(String name : recipients){
			Remote peer = list.get(name);
			Runnable worker = new DeleteContactGroupThread(peer, emisor, groupKey);
			executor.execute(worker);
		}
		executor.shutdown();
	}
	
	public void spreadNewContactGroup(String groupKey, String[] recipients, String[] newContacts){
		ExecutorService executor = Executors.newFixedThreadPool(config.MAX_POOLS_DISCONNECT_CLIENT_GROUP);
		for(String name : recipients){
			Remote peer = list.get(name);
			Runnable worker = new NewClientGroupThread(peer, newContacts, groupKey);
			executor.execute(worker);
		}
		executor.shutdown();
	}
}
