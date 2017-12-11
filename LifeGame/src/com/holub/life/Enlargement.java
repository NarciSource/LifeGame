package com.holub.life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

class Enlargement extends JPanel {
	private Cell grid;
	private static final int cellSize = 10;

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
				(  grid.widthInCells() * cellSize,
						grid.widthInCells() * cellSize
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

					grid.cellPlacement(e.getPoint(),bounds, Cell.DUMMY);
					
					repaint();
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
		
		JToolBar toolbar = new JToolBar();
			JButton clearButton = new JButton("Clear");
				clearButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						instance.clear();
						instance.repaint();
					}					
				});
			toolbar.add(clearButton);
		frame.add(toolbar, BorderLayout.NORTH);
	}
	
	public void clear()
	{
		grid.clear();
	}
	
	public void paint(Graphics g)
	{
		Rectangle panelBounds = getBounds();		
		panelBounds.x = 0;
		panelBounds.y = 0;
		grid.redraw(g, panelBounds, true);
		UnitFactory.instance().repaint();
	}
	
	
}
