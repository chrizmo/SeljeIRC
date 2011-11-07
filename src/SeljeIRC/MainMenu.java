/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

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
    
    public MainMenu(ChannelTab tab){
        super();
        tabObject = tab;
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
               
               serverConnectWindow scw = new serverConnectWindow();
               tabObject.add(scw);
               
           } 
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               tabObject.createNewTab();
               String channel = JOptionPane.showInputDialog(I18N.get("mainmenu.whichchannel"));
               // TODO     serverConnectWindow.joinChannel(String channel) -
               // send channel to connectToServer-object
           } 
        });
    }
}
