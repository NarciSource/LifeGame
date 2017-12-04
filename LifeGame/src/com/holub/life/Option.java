package com.holub.life;

public abstract class Option
{
	public static final Option HighOption = new Option()
	{
		public int 		UNIT_GRID_SIZE()				{return 16;}
		public String 	UNIT_FILE_NAME() 				{return "unit2560";}
		
		public int 		UNIVERSE_GRID_SIZE()			{return 32;}
		public int 		UNIVERSE_CELL_SIZE()			{return 2;}
		
		public int 		UNITFACTORY_GRID_WIDTH_SIZE()	{return 4;}
		public int 		UNITFACTORY_GRID_HEIGHT_SIZE()	{return 12;}
		public int 		UNITFACTORY_CELL_SIZE()			{return 5;}				
	};
	public static final Option LowOption = new Option()
	{
		public int 		UNIT_GRID_SIZE()				{return 16;}
		public String 	UNIT_FILE_NAME() 				{return "unit1280";}	
		
		public int 		UNIVERSE_GRID_SIZE()			{return 22;}
		public int 		UNIVERSE_CELL_SIZE()			{return 2;}
		
		public int 		UNITFACTORY_GRID_WIDTH_SIZE()	{return 4;}
		public int 		UNITFACTORY_GRID_HEIGHT_SIZE()	{return 4;}		
		public int 		UNITFACTORY_CELL_SIZE()			{return 5;}				
	};
	
	static Option theInstance = HighOption;
	
	public static Option 	instance()					{return theInstance;}
	public static void 		setOption(Option option)	{theInstance = option;}
	public abstract int 	UNIT_GRID_SIZE();
	public abstract String 	UNIT_FILE_NAME();
	
	public abstract int 	UNIVERSE_GRID_SIZE();
	public abstract int 	UNIVERSE_CELL_SIZE();
	
	public abstract int 	UNITFACTORY_GRID_WIDTH_SIZE();
	public abstract int 	UNITFACTORY_GRID_HEIGHT_SIZE();
	public abstract int 	UNITFACTORY_CELL_SIZE();
	
	
	
}