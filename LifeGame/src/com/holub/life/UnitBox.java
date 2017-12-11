package com.holub.life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.holub.asynch.ConditionVariable;
import com.holub.life.Cell.Memento;
import com.holub.life.Clock.Listener;
import com.holub.tools.Publisher;
import com.holub.ui.Colors;

public class UnitBox implements Cell {
	private final	int		 gridWidthSize;
	private final	int		 gridHeightSize;
	private final	int		 level;
	
	protected 		Cell[][] grid;	
	
	private 		boolean  amActive = false;
	
	private static	Publisher publisher = new Publisher();
	
	
	public UnitBox(int gridWidthSize, int gridHeightSize, Cell prototype)
	{
		this.gridWidthSize = gridWidthSize;
		this.gridHeightSize = gridHeightSize;
		
 		this.grid = new Cell[gridWidthSize][gridHeightSize];

		for( int row = 0; row < gridWidthSize; ++row )
			for( int column = 0; column < gridHeightSize; ++column )
				grid[row][column] = prototype.create();
		
		level = grid[0][0].level() + 1;
	}
	
	
	public void cellPlacement(Point here, Rectangle surface, Cell dummy)
	{		
		int pixelWidthPerCell	= surface.width	/ gridWidthSize ;
		int pixelHeightPerCell	= surface.height / gridHeightSize ;
		int row					= here.x 		/ pixelWidthPerCell ;
		int column				= here.y 		/ pixelHeightPerCell ;
		
		selecteUnit(grid[row][column]);
	}
	
	public void editPlacement(Point here, Rectangle surface, Cell dummy)
	{
		int pixelWidthPerCell 	= surface.width / gridWidthSize ;
		int pixelHeightPerCell 	= surface.height / gridHeightSize ;
		int row					= here.x 		/ pixelWidthPerCell ;
		int column				= here.y 		/ pixelHeightPerCell ;
		
		Enlargement.to(grid[row][column],row,column);
	}
	
	
	
	
	public void selecteUnit(Cell unit)
	{
		publisher.publish
		(
			new Publisher.Distributor() {
				
				public void deliverTo(Object subscriber) {
					((Listener)subscriber).selectedUnit(unit);
				}
			}
		);
		
	}	
	
	public static void addListner( Listener observer )
	{
		publisher.subscribe(observer);
	}
	
	public static interface Listener
	{	
		void selectedUnit(Cell unit);
	}
	
	
	
	
	@Override
	public void redraw(Graphics g, Rectangle here, boolean drawAll) {
		if( !amActive  && !drawAll ) return;
		
		int compoundWidth = here.width;
		int compoundHeight = here.height;
		
		Rectangle subcell = new Rectangle(	here.x, here.y,
										here.width  / gridWidthSize,
										here.height / gridHeightSize );

		for( int row = 0; row < gridWidthSize; ++row )
		{  
			for( int column = 0; column < gridHeightSize; ++column )
			{  
				grid[row][column].redraw( g, subcell, drawAll );
				subcell.translate( 0, subcell.width);
			}
			
			subcell.translate(subcell.height, -compoundHeight);
		}

		g = g.create();
		g.setColor( Colors.LIGHT_ORANGE );
		g.drawRect( here.x, here.y, here.width, here.height );

		if( amActive )
		{	
			g.setColor( Color.BLUE );
			g.drawRect(	here.x+1,	  here.y+1,
						here.width-2, here.height-2 );
		}

		g.dispose();		
	}


	public int widthInCells() {
		return gridWidthSize * grid[0][0].widthInCells();
	}
	public int heightInCells() {
		return gridHeightSize * grid[0][0].widthInCells();
	}
	public int level() {
		return level;
	}


	@Override
	public boolean figureNextState(Cell north, Cell south, Cell east, Cell west, Cell northeast, Cell northwest,
			Cell southeast, Cell southwest) {
		return false;
	}


	@Override
	public Cell edge(int row, int column) {
		return null;
	}


	@Override
	public boolean transition() {
		return false;
	}


	@Override
	public boolean isAlive() {
		return false;
	}


	@Override
	public Cell create() {
		return null;
	}


	@Override
	public Direction isDisruptiveTo() {
		return null;
	}


	@Override
	public void clear() {		
	}

	@Override
	public boolean transfer(Storable memento, Point corner, boolean load) {
		int   subcellWidth	= grid[0][0].widthInCells();
		int   subcellHeight	= grid[0][0].widthInCells();
		int   myWidth		= widthInCells();
		int	  myHeight		= heightInCells();
		Point upperLeft	= new Point( corner );

		for( int row = 0; row < gridWidthSize; ++row )
		{   
			for( int column = 0; column < gridHeightSize; ++column )
			{	
				if(grid[row][column].transfer(memento,upperLeft,load))
					amActive = true;
				upperLeft.translate( 0, subcellWidth);
			}
			upperLeft.translate( subcellHeight, -myHeight );
		}
		return amActive;
	}


	@Override
	public Storable createMemento() {
		Memento m = new UnitBoxState();
		transfer(m, new Point(0,0), Cell.STORE);
		return m;
	}


	@Override
	public Cell clone() throws CloneNotSupportedException {
		return null;
	}
	
	
	private static class UnitBoxState implements Cell.Memento
	{	Collection liveCells = new LinkedList();

		public UnitBoxState( InputStream in ) throws IOException
											{ load(in); }
		public UnitBoxState( 			   ){			}

		public void load( InputStream in ) throws IOException
		{	
			try
			{	
				ObjectInputStream source = new ObjectInputStream( in );
				liveCells = (Collection)( source.readObject() );
			}
			catch(ClassNotFoundException e)
			{
				throw new IOException("Internal Error: Class not found on load");
			}
		}

		public void flush( OutputStream out ) throws IOException
		{	
			ObjectOutputStream sink = new ObjectOutputStream(out);
			sink.writeObject( liveCells );
		}
	
		public void markAsAlive(Point location)
		{	
			liveCells.add( new Point( location ) );
		}

		public boolean isAlive(Point location)
		{	
			return liveCells.contains(location);
		}

		public String toString()
		{	
			StringBuffer b = new StringBuffer();

			b.append("UnitBoxState:\n");
			for( Iterator i = liveCells.iterator(); i.hasNext() ;)
				b.append( ((Point) i.next()).toString() + "\n" );
			return b.toString();
		}
	}
}
