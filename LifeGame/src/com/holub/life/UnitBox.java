package com.holub.life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.holub.asynch.ConditionVariable;
import com.holub.life.Clock.Listener;
import com.holub.tools.Publisher;
import com.holub.ui.Colors;

public class UnitBox implements Cell {
	private final int 	   gridWidthSize;
	private final int 	   gridHeightSize;
	
	protected Cell[][] grid;
	
	
	private boolean 		amActive = false;
	private static Publisher 		publisher = new Publisher();
	
	
	public UnitBox(int gridWidthSize, int gridHeightSize, Cell prototype)
	{
		this.gridWidthSize = gridWidthSize;
		this.gridHeightSize = gridHeightSize;
		
 		this.grid = new Cell[gridWidthSize][gridHeightSize];

		for( int row = 0; row < gridWidthSize; ++row )
			for( int column = 0; column < gridHeightSize; ++column )
				grid[row][column] = prototype.create();
	}
	
	
	public void userClicked(Point here, Rectangle surface)
	{
		
		int pixelWidthPerCell = surface.width / gridWidthSize ;
		int pixelHeightPerCell = surface.height / gridHeightSize ;
		int row				= here.x / pixelWidthPerCell ;
		int column			= here.y / pixelHeightPerCell ;
		
		int columnOffset		= here.y % pixelHeightPerCell ;
		int rowOffset	= here.x % pixelWidthPerCell ;

		Point position = new Point( rowOffset, columnOffset );
		Rectangle subcell = new Rectangle(	0, 0, 
				pixelWidthPerCell,	pixelHeightPerCell );

		disactive();
		grid[row][column].userClicked(position, subcell);
	}
	
	
	
	public void unitClicked(Point here, Rectangle surface)
	{
		int pixelWidthPerCell = surface.width / gridWidthSize ;
		int pixelHeightPerCell = surface.height / gridHeightSize ;
		int row				= here.x / pixelWidthPerCell ;
		int column			= here.y / pixelHeightPerCell ;
		
		active((Cell)grid[row][column]);
	}
	
	
	
	
	public void active(Cell grid)
	{
		publisher.publish
		(
			new Publisher.Distributor() {
				
				public void deliverTo(Object subscriber) {
					((Listener)subscriber).active(grid);
				}
			}
		);
		
	}
	public void disactive()
	{
		publisher.publish
		(
			new Publisher.Distributor() {
				
				public void deliverTo(Object subscriber) {
					((Listener)subscriber).disactive();
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
		void active(Cell grid);
		void disactive();
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


	@Override
	public int widthInCells() {
		return gridWidthSize * grid[0][0].widthInCells();
	}
	public int heightInCells() {
		return gridHeightSize * grid[0][0].widthInCells();
	}


	@Override
	public boolean figureNextState(Cell north, Cell south, Cell east, Cell west, Cell northeast, Cell northwest,
			Cell southeast, Cell southwest) {
		// TODO �ڵ� ������ �޼ҵ� ����
		return false;
	}


	@Override
	public Cell edge(int row, int column) {
		// TODO �ڵ� ������ �޼ҵ� ����
		return null;
	}


	@Override
	public boolean transition() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return false;
	}


	@Override
	public boolean isAlive() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return false;
	}


	@Override
	public Cell create() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return null;
	}


	@Override
	public Direction isDisruptiveTo() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return null;
	}


	@Override
	public void clear() {
		// TODO �ڵ� ������ �޼ҵ� ����
		
	}


	@Override
	public boolean transfer(Storable memento, Point upperLeftCorner, boolean doLoad) {
		// TODO �ڵ� ������ �޼ҵ� ����
		return false;
	}


	@Override
	public Storable createMemento() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return null;
	}


	@Override
	public Cell clone() throws CloneNotSupportedException {
		// TODO �ڵ� ������ �޼ҵ� ����
		return null;
	}
}
