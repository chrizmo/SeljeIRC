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

    public MainMenu(tabHandler tab, ConnectionHandler con) {
        super();
        channelTab = tab;
        connection = con;
        createFileMenu();
        createEditMenu();
        createHelpMenu();
        add(file);
        add(edit);
        add(help);
    }

    public void createFileMenu() {
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
                serverConnectWindow scw = new serverConnectWindow(connection);
                scw.setSize(new Dimension(400, 300));
                scw.setLocationRelativeTo(null);
                scw.pack();
                scw.setResizable(false);			// Makes sure user can't change size
                scw.setVisible(true);


            }
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {
               
               JOptionPane jop = new JOptionPane();
               
               String channel = jop.showInputDialog(I18N.get("mainmenu.whichchannel"));
                 
                    try {
                       connection.joinChannel(channel);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
             
               
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
        edit.add(getChannels);
        edit.add(colors);
        edit.add(settings);
        
        //--------------Action listeners-----------------------------

        getChannels.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
        		if(JOptionPane.showConfirmDialog(MainMenu.this, "This is really dumb and you're gonna get your stupid ass banned!\nDo you still wanna do it?" ,"Are you retarded?",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/Images/GetAllTheChannels_icon.jpeg")) == JOptionPane.YES_OPTION) //TODO: OVERSETT!
        			connection.getAllTheChannelsFromServer();
        	}
        	
        });
        
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent nils) {
                 JOptionPane.showMessageDialog(channelTab, "Settings!", "Settings", JOptionPane.PLAIN_MESSAGE);
            }
        });

        colors.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent letsMakeSomeColors) {
                Colors.colorWindow();
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
