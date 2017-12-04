package com.holub.life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UnitFactory extends JPanel {
	private static final UnitFactory theInstance = new UnitFactory();
	
	private static int  gridWidthSize;
	private static int  gridHeightSize;
	private static int  unitGridSize;
	private static int  cellSize;
	
	private 			 Cell	 unitBox;
	private static		 boolean editEnable = true;
	
	private final Dimension PREFERRED_SIZE;
	
	public Dimension size()
	{
		return PREFERRED_SIZE;
		
	}
	private UnitFactory()
	{
		gridWidthSize = Option.instance().UNITFACTORY_GRID_WIDTH_SIZE();
		gridHeightSize =Option.instance().UNITFACTORY_GRID_HEIGHT_SIZE();
		unitGridSize = 	Option.instance().UNIT_GRID_SIZE();
		cellSize =		Option.instance().UNITFACTORY_CELL_SIZE();
		
		
		unitBox = new UnitBox
				(
					gridWidthSize,
					gridHeightSize,
					new Neighborhood
					(
						unitGridSize,
						new Resident()
					)
				);
		
		PREFERRED_SIZE =
				new Dimension
				(	((UnitBox) unitBox).widthInCells() * cellSize,
					((UnitBox) unitBox).heightInCells() * cellSize
				);
		
		
		setBackground	( Color.white	 );
		setPreferredSize( PREFERRED_SIZE );
		setMaximumSize	( PREFERRED_SIZE );
		setMinimumSize	( PREFERRED_SIZE );
		setOpaque		( true			 );
		
		
		
		addMouseListener
		(	new MouseAdapter()
			{	public void mousePressed(MouseEvent e)
				{	
					Rectangle bounds = getBounds();
					bounds.x = 0;
					bounds.y = 0;				
					
					cellsPlacement(editEnable, e.getPoint(), bounds);
				}
			}
		);
		
	}
	
	public void cellsPlacement(boolean editEnable, Point point, Rectangle bounds)
	{
		if(editEnable)
		{
			unitBox.cellPlacement(point,bounds);
		}
		else
		{
			
			unitBox.unitPlacement(point,bounds,Cell.DUMMY);
		}
		repaint();
	}
	


	
	
	
	
	public static UnitFactory instance()
	{
		return theInstance;		
	}
	
	public void editSwitch()
	{
		editEnable = !editEnable;
	}
	
	public void paint(Graphics g)
	{
		Rectangle panelBounds = getBounds();

		panelBounds.x = 0;
		panelBounds.y = 0;
		unitBox.redraw(g, panelBounds, true);
	}
	
	public void doStore()
	{		
		try {
	        ObjectOutputStream out = new ObjectOutputStream(
	        							new BufferedOutputStream(
	        								new FileOutputStream(Option.instance().UNIT_FILE_NAME())
	        							)
	        						);
	        		
	        
	        out.writeObject(unitBox);
	        out.close();
	        System.out.println("store ok");	        
	        
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doLoad()
	{
		try {
	        ObjectInputStream in = new ObjectInputStream(
	        							new BufferedInputStream(
	        								new FileInputStream(Option.instance().UNIT_FILE_NAME())
	        							)
	        						);
	         
	        unitBox = (Cell) in.readObject();
	        in.close();

	        System.out.println("load ok");	         
	        

		}catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
