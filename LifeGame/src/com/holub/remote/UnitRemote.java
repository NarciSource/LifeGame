package com.holub.remote;

import java.awt.Point;
import java.awt.Rectangle;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.holub.life.Cell;

public interface UnitRemote extends Remote {
	public void cellsWriting(boolean editEnable, Point point, Rectangle bounds, Cell unit) throws RemoteException;
	public void connect(String ip, String name) throws RemoteException;
	public void startTicking(int speed) throws RemoteException;
}
