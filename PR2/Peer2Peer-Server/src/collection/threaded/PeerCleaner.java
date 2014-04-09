package collection.threaded;

import collection.SyncPeerList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerCleaner implements Runnable {

	SyncPeerList mainList;

	public PeerCleaner(SyncPeerList peerList){
		mainList = peerList;
	}
	@Override
	public void run() {
		
		/*
		 *  creamos un executor pooling
		 *  y cada x tiempo lo ejecutamos para comprobar que los peers siguen vivos
		 *  
		 *  Lo comprobamos mediante la funcion ping, esta no devuelve nada, asi que solo debemos comprobar
		 *  si salta o no la excepcion, si salta, eliminamos el peer de la lista 
		 *  informamos al resto de peers de que ese peer ya no esta
		 */

	}

}
