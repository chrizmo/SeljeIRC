package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;


/**
 * 
 * @author Hallvard Westman
 * 
 */

public class SeljeIRC extends JFrame{
  
   
	private static final long serialVersionUID = 1L; //Serializeing
        
        MainMenu mainMenu;      //Standard Menu
        
        ChannelTab channelTabs; //JTabbedPane containing all tabs
        InputField inputField;  //Standard Input field for each tab
        
        BorderLayout totalLayout; //TotalLayouts

	public SeljeIRC(){
            
            /*
             * Layout of main contentPane
             */
            totalLayout = new BorderLayout();
                setLayout(totalLayout);
            
            /*
             * JTabbedPane containging all tabs
             */
            channelTabs = new ChannelTab();
                add(channelTabs,BorderLayout.CENTER);    
            
            /*
             * Setting up the main contentPane menu
             * Passing channelTab-object for creation of new tabs
             */
            mainMenu= new MainMenu(channelTabs);
                setJMenuBar(mainMenu);
            
            /*
             * Inputfield that should be taking all input
             */
            //TODO pass channelTab-object
                
            inputField = new InputField();
                add(inputField,BorderLayout.SOUTH);
        
                
        /*
         * Basic operations on main contentPane
         */
        setVisible(true);
        pack();
       
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
    }   

public static void main(String[] args) {
    
        /*
         * Setting up the mainframe, add only functionality related to .this
         */
        
        SeljeIRC mainFrame = new SeljeIRC();    
            mainFrame.setSize(new Dimension(1200, 800));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
     
 }
}
