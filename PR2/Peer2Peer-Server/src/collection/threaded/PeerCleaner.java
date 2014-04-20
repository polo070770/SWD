package collection.threaded;

import collection.SyncPeerList;
import interficie.peer.Peer2Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.Configuration;
import server.ChatServer;


public class PeerCleaner implements Runnable {
	private Configuration config;
	private SyncPeerList mainList;
	private boolean disconnect;
	private ExecutorService executor;
	private ChatServer context;
	private ArrayBlockingQueue<String> deadPeers;
	private ConcurrentHashMap<String, Peer2Server> list;

	public PeerCleaner(SyncPeerList peerList, ChatServer context){
		config = Configuration.getInstance();
		mainList = peerList;
		disconnect = false;
		this.context = context;
		deadPeers = new ArrayBlockingQueue<String>(config.PEER_CLEANER_MAX_DEATH_PEERS_X_ROUND);
		
		

	}
	
	/**
	 * Funcion que desconecta el cleaner
	 */
	public void disconnect(){
		disconnect = true;
	}
	@Override
	public void run() {


		while(!disconnect){
			try {
				
				// recuperamos la lista de peers
				list = mainList.getPeersCopy();
				// si hay peers
				if(!list.isEmpty()){
					System.out.println("Cleaning peers!");
					// creamos el pooling
					executor = Executors.newFixedThreadPool(config.PEER_CLEANER_MAX_POOLS);

					for (String key : list.keySet()) {
						Runnable worker = new PingPeer(key, list.get(key),mainList, deadPeers);
						executor.execute(worker);
					}
					executor.shutdown();
					
					while (!executor.isTerminated()) {
							Thread.sleep(config.PEER_CLEANER_POOL_REFRESH_TIME);
					}
					
					// Eliminamos los peers
					while(!deadPeers.isEmpty()){
						String peerKey = deadPeers.take();
						System.out.println("Eliminamos " + peerKey);
						context.deletePeer(peerKey);
						//hacemos un sleep para evitar problemas de threads con la interfaz grafica
						Thread.sleep(config.PEER_CLEANER_DELETE_PEER_SLEEP);
					}
					
					
					//dormimos el hilo
				}
				// comprobamos que no han solicitado una desconexion antes de poner el hilo a dormir
				if(disconnect) break;
				Thread.sleep(config.PEER_CLEANER_SLEEP_TIME);
				// comprobamos que no han solicitado una desconexion antes iniciar la limpieza
				if(disconnect) break;
				
				
			} catch (InterruptedException e) {
				// Excepcion a la hora de dormir el hilo
				System.out.println("Excepcion a la hora de dormir el hilo");
				if(config.DEBUG)
					e.printStackTrace();
			}

		}


		

	}

}


class PingPeer implements Runnable{
	private Configuration config;
	private String key;
	private Remote peer;
	private ArrayBlockingQueue<String> deadPeers;
	
	public PingPeer(String key, Remote peer, SyncPeerList mainList, ArrayBlockingQueue<String> deadPeers) {
		config = Configuration.getInstance();
		this.key = key;
		this.peer = peer;
		this.deadPeers = deadPeers;
			
	}

	@Override
	public void run() {
		try{
			//tratamos de recibir el ping
			//System.out.println(this.key + " trying Ping");
			((Peer2Server) this.peer).ping();

		}catch(RemoteException e){
			// si hay excepcion
			System.out.println(this.key + " disconnected");
			// añadimos el peer a la lista de peers para eliminar
			synchronized(deadPeers){
				// como la lista no es ilimitada, controlamos que haya capacidad para insertar el peer en ella
				// si no hay capacidad no se añade, y se eliminara en la siguiente ronda
				if(!deadPeers.contains(this.key) && deadPeers.remainingCapacity() > 0){
					try {
						deadPeers.put(this.key);
					} catch (InterruptedException e1) {
						if(config.DEBUG) e1.printStackTrace();
					}	
				}
			}
			
		}
		
	}
	
}
