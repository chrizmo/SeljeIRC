/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private JMenu file = new JMenu("File");
    private JMenu edit = new JMenu("Edit");
    private JMenu help = new JMenu("Help");
    
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
        
    
    JMenuItem newServer = new JMenuItem("New Server");
        file.add(newServer);
    JMenuItem newChannel = new JMenuItem("New Channel");
        file.add(newChannel);
        
        //--------------Action listeners-----------------------------
        
        newServer.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
             // Create and position connection window
               serverConnectWindow scw = new serverConnectWindow();
               scw.setSize(new Dimension(400,300));
               scw.setLocationRelativeTo(null);
               scw.pack();
               scw.setVisible(true);
               
               
           } 
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               tabObject.createNewTab();
               String channel = JOptionPane.showInputDialog("Hvilken kanal?");
               // TODO     serverConnectWindow.joinChannel(String channel)
           } 
        });
    }
}
