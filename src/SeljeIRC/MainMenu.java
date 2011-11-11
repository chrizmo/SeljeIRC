
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
        add(file);
        add(edit);
        add(help);
    }
    
    public void createFileMenu(){
        
    
    JMenuItem newServer = new JMenuItem(I18N.get("mainmenu.newserver"));
        file.add(newServer);
    JMenuItem newChannel = new JMenuItem(I18N.get("mainmenu.newchannel"));
        file.add(newChannel);
        
        //--------------Action listeners-----------------------------
        
        newServer.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
             // Create and position connection window
               serverConnectWindow scw = new serverConnectWindow(connection);
               scw.setSize(new Dimension(400,300));
               scw.setLocationRelativeTo(null);
               scw.pack();
               scw.setVisible(true);
                
               
           } 
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               
               
               String channel = JOptionPane.showInputDialog(I18N.get("mainmenu.whichchannel"));
               
              //ChannelTab.setConnection(connection)
               tabObject.createNewTab(channel);
               
           } 
        });
    }
}
