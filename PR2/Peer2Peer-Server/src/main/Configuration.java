package main;

public class Configuration {
	private static Configuration INSTANCE = new Configuration();
	//booleans
	public final boolean DEBUG = false;
	
	//integers
	public final int MAX_POOLS_NEW_CLIENT = 5;
	public final int MAX_POOLS_CLIENT_DELETED = 5;
	public final int PEER_CLEANER_SLEEP_TIME = 10000;
	public final int PEER_CLEANER_POOL_REFRESH_TIME = 100;
	public final int PEER_CLEANER_MAX_POOLS = 5;
	public final int PEER_CLEANER_MAX_DEATH_PEERS_X_ROUND = 10;
	public final int PEER_CLEANER_DELETE_PEER_SLEEP = 300;
	//strings	 
    // El constructor privado no permite que se genere un constructor por defecto.
    // (con mismo modificador de acceso que la definición de la clase) 
    private Configuration() {}
 
    public static Configuration getInstance() {

        return INSTANCE;
    }
}
