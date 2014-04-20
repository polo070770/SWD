package main;

public class Configuration {
	private static Configuration INSTANCE = new Configuration();
	//booleans
	public final boolean DEBUG = true;
	
	//integers
	public final int MAX_POOLS_NEW_CLIENT = 5;
	public final int MAX_POOLS_NEW_GROUP = 5;
	public final int MAX_POOLS_NEW_MESSAGE_GROUP = 5;
	public final int MAX_POOLS_DISCONNECT_CLIENT_GROUP = 5;
	public final int MAX_POOLS_NEW_CLIENT_GROUP = 5;
	public final int MAX_POOLS_CLIENT_DELETED = 5;
	//strings	 
    // El constructor privado no permite que se genere un constructor por defecto.
    // (con mismo modificador de acceso que la definición de la clase) 
    private Configuration() {}
 
    public static Configuration getInstance() {

        return INSTANCE;
    }
}
