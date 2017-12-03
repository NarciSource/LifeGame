package com.holub.life;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.holub.remote.UnitRemote;
import com.holub.remote.UnitRemoteImpl;
import com.holub.ui.MenuSite;

/*******************************************************************
 * An implemenation of Conway's Game of Life.
 *
 * @include /etc/license.txt
 */

public final class Life extends JFrame
{	
	private static String 		name;
	private static InetAddress 	ip;

	public static void main( String[] arguments )
	{
		new Life();
	}

	private Life()
	{	
		super( "Startegy Game of Life. "
					+"(c)2017 Jeong Won-Cheol. 20113560. Chung-Ang University."
					+"<https://github.com/NarciSource/LifeGame> ~based on <http://www.holub.com>");

		try {
			name = JOptionPane.showInputDialog("플레이어의 이름을 입력하세요.");
			ip = InetAddress.getLocalHost();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		UnitRemoteImpl.resister(name);
		
		Universe.instance();
		UnitFactory.instance();

		layoutInit();
	}
	
	private void layoutInit()
	{
		setDefaultCloseOperation	( EXIT_ON_CLOSE 		);
		getContentPane().setLayout	( new BorderLayout(0,0)	);
		
		
		/** Menu bar */
		// Must establish the MenuSite very early in case
		// a subcomponent puts menus on it.
		MenuSite.establish( this );
			MenuSite.addLine( this, "Grid", "Clear",
				new ActionListener() {	
					public void actionPerformed(ActionEvent e)
					{	
						Universe.instance().clear();
						repaint();
					}
				}
			);
			MenuSite.addLine(this, "Grid", "Load",		// {=Universe.load.setup}
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{	
						Universe.instance().doLoad();
					}
				}
			);
			MenuSite.addLine(this, "Grid", "Store",
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{	
						Universe.instance().doStore();
					}
				}
			);
			MenuSite.addLine(this, "Grid", "Exit",
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
			        {	
						System.exit(0);
			        }
				}
			);
			
		Clock.instance().addClockListener //{=Universe.clock.subscribe}
		(	new Clock.Listener()
			{	public void tick()
				{	
					Universe.instance().tick();
				}
			}
		);
		
		
		/** Tool bar */
		JToolBar toolbar = new JToolBar("tool bar");
		getContentPane().add(toolbar, BorderLayout.NORTH);
			toolbar.setBackground(Color.LIGHT_GRAY);
			
			
			JButton startButton = new JButton("Start");
				startButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Clock.instance().startTicking(150);
					}
				});
			toolbar.add(startButton);
			
			
			toolbar.addSeparator();
			
			
			JButton editButton = new JButton("Edit");
				editButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						editSwitch();
		                
		                if (editButton.getText().equals("Edit")) {
		                	editButton.setText("Unit");
		                }
		                else {
		                	editButton.setText("Edit");
		                }
					}
				});
			toolbar.add(editButton);
						
			
			JButton multiButton = new JButton("Multi");
				multiButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String opponentIp = JOptionPane.showInputDialog("상대방의 ip를 입력하세요");
						String opponentName = JOptionPane.showInputDialog("상대방의 이름을 입력하세요");
						
						multiMode(opponentIp, opponentName);
					}
				});
			toolbar.add(multiButton);
			
		
			toolbar.add(new Label("Host/IP = " + ip + "   User> " + name));

		
		
		
		
		/** Contentes */
		JLayeredPane layeredPane = new JLayeredPane();		
			layeredPane.setLayout(new BoxLayout(layeredPane, BoxLayout.X_AXIS));		
		
			
			layeredPane.add( Universe.instance(), BorderLayout.CENTER);
			
			
			JPanel marginPanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) marginPanel.getLayout();
				flowLayout.setVgap(250);
				flowLayout.setHgap(50);
			layeredPane.add(marginPanel);
			
			
			layeredPane.add( UnitFactory.instance(), BorderLayout.CENTER);
			
			
		getContentPane().add(layeredPane, BorderLayout.CENTER);
		
		pack();
		setVisible( true );
	}
	
	private void editSwitch()
	{
		Universe.instance().editSwitch();
        UnitFactory.instance().editSwitch();
	}
	
	private void multiMode(String opIp, String opName)
	{
		UnitRemoteImpl.duplexConnect(this.ip.getHostAddress().toString(), this.name,
									opIp, opName);
	}
}
