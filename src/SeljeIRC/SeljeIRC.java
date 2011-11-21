package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.text.BadLocationException;


/**
 * 
 * @author Hallvard Westman
 * 
 */

public class SeljeIRC extends JFrame{
  
   
	private static final long serialVersionUID = 1L; //Serializeing
        
        MainMenu mainMenu;      //Standard Menu
        
        static tabHandler channelTabs; //JTabbedPane containing all tabs
        static InputField inputField;  //Standard Input field for each tab
        BorderLayout totalLayout; //TotalLayouts
        static Colors colorSettings; // Colorsettings
        
        static ConnectionHandler connection;
        
        boolean isConnected;

	public SeljeIRC() throws BadLocationException{
            
            /*
             * Connection to server
             */
            
            channelTabs = new tabHandler();
            connection = new ConnectionHandler(channelTabs);
            colorSettings = new Colors();

            
            /*
             * Layout of main contentPane
             */
            totalLayout = new BorderLayout();
                setLayout(totalLayout);
            
            /*
             * JTabbedPane containging all tabs
             */
            
                add(channelTabs,BorderLayout.CENTER);    
                
                
            
            /*
             * Setting up the main contentPane menu
             * Passing channelTab-object for creation of new tabs
             */
            mainMenu= new MainMenu(channelTabs,connection);
                setJMenuBar(mainMenu);
            
            /*
             * Inputfield that should be taking all input
             */
            //TODO pass channelTab-object
                
            
        
                
        /*
         * Basic operations on main contentPane
         */
        setVisible(true);
        pack();
        
        this.addWindowListener(new WindowAdapter()
        {
        public void windowClosing(WindowEvent e)
        {
        // your stuf here
           try{ 
           connection.closeConnection(); 
           }catch(Exception ex){
               System.out.printf("exception onclose");
           }
            
        //System.exit(0);
        }
        });
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
        public static ConnectionHandler returnConnection(){
            return connection;
        }

        
        
        
        
public static void main(String[] args) throws BadLocationException {
    
        /*
         * Setting up the mainframe, add only functionality related to .this
         */
        
    SplashScreen splashScreen = new SplashScreen("logo2.jpg");
    splashScreen.splash();
    try {
      Thread.sleep(0); //TODO: CHANGE BACK TO 3000
    }
    catch(InterruptedException ex) {
      System.out.println(ex);
    }
    splashScreen.setVisible(false);
    
    
    
        SeljeIRC mainFrame = new SeljeIRC();    
            mainFrame.setSize(new Dimension(1200, 800));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
     
 }
}
