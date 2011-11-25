package SeljeIRC;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
    public static MainMenu mainMenu;      //Standard Menu
    static InputField inputField;  //Standard Input field for each tab
    BorderLayout totalLayout; //TotalLayouts
    static Colors colorSettings; // Colorsettings
    public static tabHandler channelTabObj; //JTabbedPane containing all tabs        
    public static ConnectionHandler connectionHandlerObj;    
    boolean isConnected;

    public SeljeIRC(String title) throws BadLocationException{
        super(title);

        connectionHandlerObj = ConnectionHandler.getInstance();
        channelTabObj = tabHandler.getInstance();
        colorSettings = new Colors();


        /*
         * Layout of main contentPane
         */
        totalLayout = new BorderLayout();
        setLayout(totalLayout);
        
            
        
        /*
         * JTabbedPane containging all tabs
         */
        this.setBackground(new Color(224,224,224));
            add(channelTabObj,BorderLayout.CENTER);    

        /*
         * Setting up the main contentPane menu
         * Passing channelTab-object for creation of new tabs
         */
        mainMenu= new MainMenu();
            setJMenuBar(mainMenu);
        
        /*
         * toolbar
         */
        
        ToolBar tb = new ToolBar();
        add(tb,BorderLayout.NORTH);
        
        /*
         * Inputfield that should be taking all input
         */
        //TODO pass channelTab-object
                
   
                
        /*
         * Basic operations on main contentPane
         */
        setVisible(true);
        pack();
        

        this.addWindowListener(new WindowAdapter()   {
            public void windowClosing(WindowEvent e)   {
        
                try{ 
                    connectionHandlerObj.closeConnection(); 
                }catch(Exception ex){
                    System.out.printf("exception onclose");
                }
            }
        });
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }


        /**
         * Returns current connectionHandlerObject
         * @author Hallvard Westman
         * @return connectionHandlerObj Returns current connection
         */
        public static ConnectionHandler returnConnection(){
            return connectionHandlerObj;
        }
        public void setStatusFocus(){
            SingleTab st = (SingleTab) channelTabObj.getComponent(1);
            st.passFocusToField();
        }

        
  
    public static void main(String[] args) throws BadLocationException {
    
        /*
         * Setting up the mainframe, add only functionality related to .this
         *
        
    */
        try {
            UIManager.getSystemLookAndFeelClassName();
        } catch (Exception ex) {
            Logger.getLogger(SeljeIRC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
          Toolkit toolkit =  Toolkit.getDefaultToolkit ();
          Dimension dim = toolkit.getScreenSize();
    
    
        SeljeIRC mainFrame = new SeljeIRC("SeljeIRC");    
        mainFrame.setVisible(false);
        mainFrame.setBounds(0,0,dim.width, dim.height-100);
        mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        SplashScreen splashScreen = new SplashScreen("logo2.jpg");
        splashScreen.splash();
        
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

