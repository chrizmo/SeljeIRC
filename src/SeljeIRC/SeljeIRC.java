package SeljeIRC;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	public SeljeIRC(String title) throws BadLocationException{
            super(title);
            
       
            
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
        public void setStatusFocus(){
            SingleTab st = (SingleTab) channelTabs.getComponent(1);
            st.passFocusToField();
        }

        
        
        
        
public static void main(String[] args) throws BadLocationException {
    
        /*
         * Setting up the mainframe, add only functionality related to .this
         *
        
        
    
         //NimRODTheme nt = new NimRODTheme();
         
         try{
             UIManager.setLookAndFeel(new com.nilo.plaf.nimrod.NimRODLookAndFeel());
         }catch(Exception e){
             
         }
   
    */
    
        // GUL : Color(255,255,0)
        // MØRKBRUN : Color(65,52,0)
        // LYSEBRUN : Color(130,110,39)
    
        NimRODTheme nt = new NimRODTheme();
        //nt.setPrimary(Color.green);
        //nt.setPrimary1(Color.BLUE);
        nt.setPrimary2(Color.black); // tabcolor and hover
        nt.setPrimary3(Color.GRAY); // listbackground
        //nt.setSecondary(Color.GREEN);
        //nt.setSecondary1(Color.ORANGE); // border
        //nt.setSecondary2(Color.PINK);
        nt.setSecondary3(new Color(35,28,2));    // background
        
        
        nt.setBlack(new Color(255,255,0)); //text
        nt.setWhite(Color.black); //textfields
        //nt.setBlack(Color.cyan);
        
        

        NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
        NimRODLF.setCurrentTheme( nt);
        try {
            UIManager.setLookAndFeel( NimRODLF);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SeljeIRC.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        SeljeIRC mainFrame = new SeljeIRC("SeljeIRC");
            
            
            mainFrame.setVisible(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            mainFrame.setBounds(0,0,screenSize.width, screenSize.height);
            //mainFrame.setSize(new Dimension(1200, 800));
            
            
            mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
            
             SplashScreen splashScreen = new SplashScreen("logo2.jpg");
                splashScreen.splash();
                //splashScreen.setVisible(false);
            
            
            try {
                  Thread.sleep(1000); //TODO: CHANGE BACK TO 3000
                }
                catch(InterruptedException ex) {
                  System.out.println(ex);
                }
            splashScreen.setVisible(false)  ;
            mainFrame.setVisible(true);
            try {
                  Thread.sleep(100); //gotta wait to set focus
                }
                catch(InterruptedException ex) {
                  System.out.println(ex);
                }
            mainFrame.setStatusFocus();
     
 }
}

