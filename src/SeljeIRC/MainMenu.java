
package SeljeIRC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author wbserver
 */
public class MainMenu extends JMenuBar {
    private JMenu file = new JMenu(I18N.get("mainmenu.file"));
    private JMenu edit = new JMenu(I18N.get("mainmenu.edit"));
    private JMenu help = new JMenu(I18N.get("mainmenu.help"));
    
    ChannelTab tabObject;
    ConnectionHandler connection;
    
    public MainMenu(ChannelTab tab, ConnectionHandler con){
        super();
        tabObject = tab;
        connection = con;
        createFileMenu();
        createEditMenu();
        createHelpMenu();
        add(file);
        add(edit);
        add(help);
    }
    
    public void createFileMenu(){
        
    
    JMenuItem newServer = new JMenuItem(I18N.get("mainmenu.newserver"));
        file.add(newServer);
    JMenuItem newChannel = new JMenuItem(I18N.get("mainmenu.newchannel"));
        file.add(newChannel);
    JMenuItem exitProgram = new JMenuItem(I18N.get("mainmenu.close"));
    	file.add(exitProgram);
        
        //--------------Action listeners-----------------------------
        
        newServer.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
             // Create and position connection window
               serverConnectWindow scw = new serverConnectWindow(connection);
               scw.setSize(new Dimension(400,300));
               scw.setLocationRelativeTo(null);
               scw.pack();
               scw.setResizable(false);			// Makes sure user can't change size
               scw.setVisible(true);
                
               
           } 
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               if(connection.connectedToServer()){
               
               String channel = JOptionPane.showInputDialog(I18N.get("mainmenu.whichchannel"));
               
              //ChannelTab.setConnection(connection)
               
               tabObject.createNewTab(channel);
               }
               else
               tabObject.updateStatusScreen("Cant join when not connected");
               
           } 
        });
        
        exitProgram.addActionListener(new ActionListener(){ // Closes the program
        	public void actionPerformed (ActionEvent evt){
        		try{
        			System.exit(0);
        		}catch(SecurityException secE){
        			System.err.println("Error during exit" + secE.getMessage());
        		}
        	}
        });
    }

     public void createEditMenu(){


    JMenuItem settings = new JMenuItem(I18N.get("mainmenu.settings"));
        edit.add(settings);

        //--------------Action listeners-----------------------------

        settings.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
             



           }
        });

    }

          public void createHelpMenu(){


    JMenuItem helpItem = new JMenuItem(I18N.get("mainmenu.help"));
        help.add(helpItem);
    JMenuItem aboutItem = new JMenuItem(I18N.get("mainmenu.about"));
        help.add(aboutItem);

        //--------------Action listeners-----------------------------

        help.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {

           }
        });

    } // End of createHelpMenu

}
