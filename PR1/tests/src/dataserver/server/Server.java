package dataserver.server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
	public static void main(String[] args) throws Exception {
		int port = 1099;
		LocateRegistry.createRegistry(port);
		
		DateImpl dateServer = new DateImpl();
		String url = "rmi://localhost:"+port+"/";
		Naming.rebind(url+"DateServer", dateServer);
		System.out.println(Naming.list(url)[0]); 
	}
}