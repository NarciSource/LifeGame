package com.holub.life;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
					+"(c) 2017 Jeong Won-Cheol. 20113560");

		try {
			name = JOptionPane.showInputDialog("플레이어의 이름을 입력하세요.");
			if(name.isEmpty()) name ="unkown";			
			
			ip = InetAddress.getLocalHost();
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			JOptionPane.showMessageDialog(null, "RMI resister okay."+" port is "+Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "RMI resister fail");
			e.printStackTrace();
		}
		
		UnitRemoteImpl.resister(name);
		
		Universe.instance();
		UnitFactory.instance();
		
		UnitFactory.instance().doLoad();

		layoutInit();
	}
	
	private Label mode = new Label("Single Mode");
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
			MenuSite.addLine(this, "Grid", "Load",
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{	
						Universe.instance().doLoad();
						repaint();
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
			
			MenuSite.addLine(this, "Help", "About",
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						JOptionPane.showMessageDialog(null, 
								"Strategy LifeGame (c) 2017\n"
								+"dev. Jeong WonCheol\n"
								+"StudentID. 20113560\n"
								+"Univ. Chung-Ang. Seoul, Korea\n"
								+"gitHub: https://github.com/NarciSource/LifeGame\n"
								+"based on: http://www.holub.com ");
					}
				}
			);
		
		/** Clock */
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
			toolbar.setBackground(Color.LIGHT_GRAY);			
			
			JButton startButton = new JButton(new ImageIcon("img/play.png"));
				
				startButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Clock.instance().startTicking(150);
						repaint();
					}
				});
			toolbar.add(startButton);
			
			
			toolbar.addSeparator();
						
			
			JButton multiButton = new JButton(new ImageIcon("img/connect.png"));
				multiButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String opponentIp = JOptionPane.showInputDialog("상대방의 ip를 입력하세요");
						String opponentName = JOptionPane.showInputDialog("상대방의 이름을 입력하세요");
						
						multiMode(opponentIp, opponentName);
						
						multiButton.setEnabled(false);
					}
				});				
			toolbar.add(multiButton);
			
			toolbar.addSeparator();
		
			toolbar.add(new Label("Host/IP = " + ip + "    User = " + name));
			
			toolbar.add(mode);
			
			toolbar.addSeparator();
			
			
			JButton editButton = new JButton("Edit", new ImageIcon("img/edit.png"));
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
		
			
			
			
		getContentPane().add(toolbar, BorderLayout.NORTH);
		
		
		
		
		/** Contentes */
		JLayeredPane layeredPane = new JLayeredPane();		
			layeredPane.setLayout(new BoxLayout(layeredPane, BoxLayout.X_AXIS));		
		
			
			layeredPane.add( Universe.instance(), BorderLayout.CENTER);
			
			
			layeredPane.add(new JPanel());
			
			
			JLayeredPane layeredPane2 = new JLayeredPane();
				layeredPane2.setLayout(new BorderLayout(2, 0));
				
				layeredPane2.add(new JLabel(new ImageIcon("img/unitbox.png")), BorderLayout.NORTH);
				
				layeredPane2.add(UnitFactory.instance(), BorderLayout.SOUTH);
			layeredPane.add(layeredPane2);
			
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
		
		if(UnitRemoteImpl.isConnected())
			mode.setText("Multi Mode");
		else
			mode.setText("Single Mode");
	}
	
}
