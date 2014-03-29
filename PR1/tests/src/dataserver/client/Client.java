package dataserver.client;
import dataserver.DateInterface;
import java.rmi.Naming; import java.util.Date;

public class Client {
	public static void main(String[] args) throws Exception { if (args.length != 1)
		throw new IllegalArgumentException("Syntax: DateClient <hostname>");
		DateInterface dateServer = (DateInterface) Naming.lookup("rmi://"+ args[0]+":1099/DateServer");
		Date when = dateServer.getDate(); 
		System.out.println(when);
	}
}