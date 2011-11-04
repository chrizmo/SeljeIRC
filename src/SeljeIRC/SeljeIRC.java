package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;


/**
 * 
 * @author Hallvard Westman
 */

public class SeljeIRC extends JFrame{
    
   
	private static final long serialVersionUID = 1L;

	public SeljeIRC(){
       
        BorderLayout totalLayout = new BorderLayout();
        setLayout(totalLayout);
        
        MainMenu m = new MainMenu();
        setJMenuBar(m);
        
        ChannelTab channelTabs = new ChannelTab();
            channelTabs.setSize(new Dimension(200,200));
            channelTabs.setBackground(Color.yellow);
       
        add(channelTabs,BorderLayout.CENTER);
        
        InputField inputField = new InputField();
        add(inputField,BorderLayout.SOUTH);
        
        setVisible(true);
        pack();
       
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
    }   

public static void main(String[] args) {
    
    
        SeljeIRC mainFrame = new SeljeIRC();    
            mainFrame.setSize(new Dimension(1200, 800));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
     
 }
}
