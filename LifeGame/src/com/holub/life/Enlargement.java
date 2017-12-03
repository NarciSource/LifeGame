package com.holub.life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Enlargement extends JPanel {
	private Cell grid;
	private static final int DEFAULT_CELL_SIZE = 10;

	public static void to(Cell inputCell,int row,int column)
	{
		String title = "UnitFactory = ("+row+" , "+column + ")";
		selfWindow(new Enlargement(inputCell, row, column),title);
	}
	private Enlargement(Cell inputCell,int row,int column)
	{
		
		grid = inputCell;
		
		final Dimension PREFERRED_SIZE =
				new Dimension
				(  grid.widthInCells() * DEFAULT_CELL_SIZE,
						grid.widthInCells() * DEFAULT_CELL_SIZE
				);
	
		setBackground	( Color.red	 );
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

					grid.cellPlacement(e.getPoint(),bounds);
					
					repaint();
					UnitFactory.instance().repaint();
				}
			}
		);		
	}
	
	private static void selfWindow(Enlargement instance, String title)
	{
		final int FRAME_SIZE = 500;
		
		JFrame frame = new JFrame(title);
		
		frame.setSize(FRAME_SIZE,FRAME_SIZE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				frame.setVisible(false);
				frame.dispose();
			}
		});
		frame.add(instance);
		frame.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		Rectangle panelBounds = getBounds();		
		panelBounds.x = 0;
		panelBounds.y = 0;
		grid.redraw(g, panelBounds, true);
	}
	
	
}
