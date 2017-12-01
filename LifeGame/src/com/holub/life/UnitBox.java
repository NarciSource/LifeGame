package com.holub.life;

import java.awt.Point;
import java.awt.Rectangle;

import com.holub.life.Clock.Listener;
import com.holub.tools.Publisher;

public class UnitBox extends Neighborhood {

	private final int 	   	gridSize;
	private boolean 		amActive = false;
	private static Publisher 		publisher = new Publisher();
	
	
	public UnitBox(int gridSize, Cell prototype) {
		super(gridSize, prototype);
		
		this.gridSize = gridSize;
	}
	
	public void userClicked(Point here, Rectangle surface)
	{
		int pixelsPerCell = surface.width / gridSize ;
		int row				= here.y     	/ pixelsPerCell ;
		int column			= here.x     	/ pixelsPerCell ;
		int rowOffset		= here.y     	% pixelsPerCell ;
		int columnOffset	= here.x     	% pixelsPerCell ;

		Point position = new Point( columnOffset, rowOffset );
		Rectangle subcell = new Rectangle(	0, 0, pixelsPerCell,
												  pixelsPerCell );

		disactive();
		grid[row][column].userClicked(position, subcell);
		amActive = true;
	}
	
	public void unitClicked(Point here, Rectangle surface)
	{
		int pixelsPerCell = surface.width / gridSize ;
		int row				= here.y     	/ pixelsPerCell ;
		int column			= here.x     	/ pixelsPerCell ;
		
		amActive = true;
		
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
}
