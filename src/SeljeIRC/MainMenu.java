package SeljeIRC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author wbserver
 */
public class MainMenu extends JMenuBar {

    private JMenu file = new JMenu(I18N.get("mainmenu.file"));
    private JMenu edit = new JMenu(I18N.get("mainmenu.edit"));
    private JMenu help = new JMenu(I18N.get("mainmenu.help"));

    
    tabHandler channelTab;
    ConnectionHandler connection;

    public MainMenu() {
        super();
        createFileMenu();
        createEditMenu();
        createHelpMenu();
        add(file);
        add(edit);
        add(help);
    }

    public void createFileMenu() {
    	
    	channelTab = SeljeIRC.channelTabObj.getInstance();
    	connection = SeljeIRC.connectionHandlerObj.getInstance();
    	
        JMenuItem newServer = new JMenuItem(I18N.get("mainmenu.newserver"));
        file.add(newServer);
        JMenuItem newChannel = new JMenuItem(I18N.get("mainmenu.newchannel"));
        file.add(newChannel);


        
        JMenuItem exitProgram = new JMenuItem(I18N.get("mainmenu.close"));
        file.add(exitProgram);

        //--------------Action listeners-----------------------------

        newServer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                // Create and position connection window
                serverConnectWindow scw = new serverConnectWindow();
                scw.setSize(new Dimension(400, 300));
                scw.setLocationRelativeTo(null);
                scw.pack();
                scw.setResizable(false);			// Makes sure user can't change size
                scw.setVisible(true);


            }
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               if(connection.connectedToServer()){
               JOptionPane jop = new JOptionPane();
               
               String channel = jop.showInputDialog(I18N.get("mainmenu.whichchannel"));
                 
                    try {
                        connection.joinChannel(channel);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
               else
                   channelTab.updateStatusScreen("Gotta connect to server first");
                 
           } 
        });

        exitProgram.addActionListener(new ActionListener() { // Closes the program

            public void actionPerformed(ActionEvent evt) {
                try {
                    System.exit(0);
                } catch (SecurityException secE) {
                    System.err.println("Error during exit" + secE.getMessage());
                }
            }
        });
    }

    public void createEditMenu() {


        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem getChannels = new JMenuItem("Get channel list");
        JMenuItem colors = new JMenuItem("Colors");
        JMenuItem changeNick = new JMenuItem("Change nick");
        edit.add(getChannels);
        edit.add(colors);
        edit.add(changeNick);
        edit.add(settings);
        
        //--------------Action listeners-----------------------------

        getChannels.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
                    
                    if(connection.connectedToServer()){
        		if(JOptionPane.showConfirmDialog(MainMenu.this, I18N.get("mainmenu.getallchannels") ,I18N.get("mainmenu.getallchannelsheader"),JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/Images/GetAllTheChannels_icon.jpeg")) == JOptionPane.YES_OPTION) //TODO: OVERSETT!
        			connection.getAllTheChannelsFromServer();
                     }
                   else
                       channelTab.updateStatusScreen("Gotta connect to server first");
               }
        	
        });
        
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent nils) {
                 JOptionPane.showMessageDialog(channelTab, "Settings!", "Settings", JOptionPane.PLAIN_MESSAGE);	// TODO: Oversett
            }
        });

        colors.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent letsMakeSomeColors) {
                Colors.colorWindow();
            }
        });
        
        changeNick.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if(connection.connectedToServer()){
                String newNick = JOptionPane.showInputDialog("New nick");
                if (newNick != null)
                    connection.getCurrentSession().changeNick(newNick);
                }
                else
                       channelTab.updateStatusScreen("Gotta connect to server first");
            }
        });

    }

    public void createHelpMenu() {
        JMenuItem helpItem = new JMenuItem(I18N.get("mainmenu.help"));
        help.add(helpItem);
        JMenuItem aboutItem = new JMenuItem(I18N.get("mainmenu.about"));
        help.add(aboutItem);

        //--------------Action listeners-----------------------------

        helpItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(channelTab, "Oh wait, you wanted"
                        + " help? Well, maybe later...");
            }
        });

        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(channelTab, "SeljeIRC is an "
                        + "IRC-client written in Java by:\n"
                        + "Jon Arne Westgaard, Lars Erik Pedersen, "
                        + "Christer Vaskinn and Hallvard Westman.\n\n"
                        + "This program is made of 100% recycled bytes,\n"
                        + "and contains no additives apart from unused imports"
                        + " and a few nullpointer exceptions.",
                        "About", JOptionPane.PLAIN_MESSAGE);

            }
        });

    } // End of createHelpMenu
}
