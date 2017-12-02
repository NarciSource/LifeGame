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
	private boolean editUnit=true;
	private static String name;

	public static void main( String[] arguments )
	{	
		
		name = JOptionPane.showInputDialog("이름을 입력하세요.");
		
		UnitRemoteImpl.resister(name);
		  
		
		
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		new Life();
	
	
		
	}

	private Life()
	{	super( "The Game of Life. "
					+"(c)2003 Allen I. Holub <http://www.holub.com>");

		// Must establish the MenuSite very early in case
		// a subcomponent puts menus on it.
		MenuSite.establish( this );		//{=life.java.establish}

		setDefaultCloseOperation	( EXIT_ON_CLOSE 		);
		getContentPane().setLayout	( new BorderLayout(0,0)	);
		
		
		JToolBar toolbar = new JToolBar("tool bar");
		getContentPane().add(toolbar, BorderLayout.NORTH);
		toolbar.setBackground(Color.LIGHT_GRAY);
		toolbar.add(new JButton("Start"));
		toolbar.addSeparator();
		toolbar.add(new JButton("Edit"));
		
		JButton multiButton = new JButton("Multi");
		toolbar.add(multiButton);
		multiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = JOptionPane.showInputDialog("상대방의 ip");
				String name = JOptionPane.showInputDialog("상대방의 이름");
				
				UnitRemoteImpl.lookup(ip, name);
			}
			
			
		});
		
		

		
		
		
		
		
		JLayeredPane layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(new BoxLayout(layeredPane, BoxLayout.X_AXIS));
		
		
			layeredPane.add( Universe.instance(), BorderLayout.CENTER); //{=life.java.install}
			
			JPanel marginPanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) marginPanel.getLayout();
			flowLayout.setVgap(250);
			flowLayout.setHgap(50);
			layeredPane.add(marginPanel);
			
			layeredPane.add( UnitFactory.instance(), BorderLayout.CENTER);
		
		
		
		JButton button1 = new JButton("Enable Edit");
		
		getContentPane().add(button1,BorderLayout.EAST);
		button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                
                Universe.instance().setEditTrigger();
                UnitFactory.instance().setEditTrigger();
                
                
                if (b.getText().equals("Enable Edit"))
                    b.setText("Disable Edit");
                else
                    b.setText("Enable Edit");                
            }
        });
		
		pack();
		setVisible( true );
	}
}
