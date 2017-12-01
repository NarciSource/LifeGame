package com.holub.life;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import com.holub.ui.MenuSite;

/*******************************************************************
 * An implemenation of Conway's Game of Life.
 *
 * @include /etc/license.txt
 */

public final class Life extends JFrame
{	
	private boolean editUnit=true;

	public static void main( String[] arguments )
	{	new Life();
	}

	private Life()
	{	super( "The Game of Life. "
					+"(c)2003 Allen I. Holub <http://www.holub.com>");

		// Must establish the MenuSite very early in case
		// a subcomponent puts menus on it.
		MenuSite.establish( this );		//{=life.java.establish}

		setDefaultCloseOperation	( EXIT_ON_CLOSE 		);
		getContentPane().setLayout	( new GridLayout(1,2)	);
		getContentPane().add( Universe.instance(), BorderLayout.CENTER); //{=life.java.install}
		getContentPane().add( UnitFactory.instance(), BorderLayout.CENTER);
		
		
		
		JButton button1 = new JButton("Enable Edit");
		this.add(button1);
		button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                
                editUnit = !editUnit;
                
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
