package dataserver.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import dataserver.DateInterface;

public class DateImpl extends UnicastRemoteObject implements DateInterface {
	
	public DateImpl() throws RemoteException {
		System.out.println("constructor dataserver");
	}

	public Date getDate() throws RemoteException {
		System.out.println("Function invoqued");
		return new Date();
	}
}