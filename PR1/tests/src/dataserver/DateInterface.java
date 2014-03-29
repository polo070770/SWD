package dataserver;

import java.rmi.*;
import java.util.*;

public interface DateInterface extends Remote {
   Date getDate() throws RemoteException;
}