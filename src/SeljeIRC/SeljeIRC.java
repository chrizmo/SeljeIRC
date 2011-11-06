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
        
        ChannelTab channelTabs;
        MainMenu m;
        BorderLayout totalLayout;
        InputField inputField;

	public SeljeIRC(){
       
        totalLayout = new BorderLayout();
            setLayout(totalLayout);
            
        channelTabs = new ChannelTab();
            add(channelTabs,BorderLayout.CENTER);    
        
        m= new MainMenu(channelTabs);
            setJMenuBar(m);
        
        
        
        inputField = new InputField();
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
