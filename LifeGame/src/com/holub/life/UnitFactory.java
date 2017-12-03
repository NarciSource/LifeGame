package com.holub.life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UnitFactory extends JPanel {
	private static final UnitFactory theInstance = new UnitFactory();
	
	private static final int  LAYER1_GRID_WIDTH_SIZE = 4;
	private static final int  LAYER1_GRID_HEIGHT_SIZE = 12;
	private static final int  LAYER2_GRID_SIZE = 16;
	private static final int  DEFAULT_CELL_SIZE = 5;
	
	private		   final Cell	 unitBox;
	private static		 boolean editEnable = true;
	
	private UnitFactory()
	{
		unitBox = new UnitBox
				(
					LAYER1_GRID_WIDTH_SIZE,
					LAYER1_GRID_HEIGHT_SIZE,
					new Neighborhood
					(
						LAYER2_GRID_SIZE,
						new Resident()
					)
				);
		
		final Dimension PREFERRED_SIZE =
				new Dimension
				(	((UnitBox) unitBox).widthInCells() * DEFAULT_CELL_SIZE,
					((UnitBox) unitBox).heightInCells() * DEFAULT_CELL_SIZE
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
		Rectangle clipBounds  = g.getClipBounds();

		panelBounds.x = 0;
		panelBounds.y = 0;
		unitBox.redraw(g, panelBounds, true);
	}
}
