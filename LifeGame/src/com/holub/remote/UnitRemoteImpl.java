package com.holub.remote;

import java.awt.Point;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import com.holub.life.Cell;
import com.holub.life.Universe;

public class UnitRemoteImpl extends UnicastRemoteObject implements UnitRemote {
	
	private static final long serialVersionUID = 1L;
	private static UnitRemote theInstance = null;
	private static UnitRemote service = null;
	
	/*singleton*/
	private UnitRemoteImpl() throws RemoteException {}
	
	public static UnitRemote instance()
	{
		return theInstance;		
	}
	
	public static void resister(String name)
	{
		try {
			theInstance = new UnitRemoteImpl();
			Naming.rebind(name, theInstance);
			
			
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	
	public static void duplexConnect(String myIp, String myName,
									String opIp, String opName)
	{
		try {
			UnitRemoteImpl.instance().connect(opIp, opName);
			
			service.connect(myIp, myName);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void connect(String ip, String name)
	{
		if(service == null)
		{
			try {
				service = (UnitRemote) Naming.lookup("rmi://"+ip+"/"+name);
				
				
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static UnitRemote require()
	{
		return service;		
	}

	@Override
	public void cellsWriting(boolean editEnable, Point point, Rectangle bounds, Cell unit) throws RemoteException {
		Universe.instance().cellsPlacement(editEnable, point, bounds, unit);
	}
}
