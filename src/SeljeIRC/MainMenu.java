package SeljeIRC;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.help.HelpSet;


/**
 * Set's ut the main-menu
 * @author Hallvard Westman
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
        file.setMnemonic('F');
        edit.setMnemonic('E');
        help.setMnemonic('H');
        add(file);
        add(edit);
        add(help);
    }

    /**
     * Creates the file-menu
     */
    public void createFileMenu() {
    	
    	channelTab = SeljeIRC.channelTabObj.getInstance();
    	connection = SeljeIRC.connectionHandlerObj.getInstance();

        JMenuItem newServer = new JMenuItem(I18N.get("mainmenu.newserver"), new ImageIcon("newserver.jpeg"));
        newServer.setMnemonic('S');
        file.add(newServer);
        JMenuItem newChannel = new JMenuItem(I18N.get("mainmenu.newchannel"));
        newChannel.setMnemonic('C');
        file.add(newChannel);


        
        JMenuItem exitProgram = new JMenuItem(I18N.get("mainmenu.close"));
        exitProgram.setMnemonic('Q');
        file.add(exitProgram);

        //--------------Action listeners-----------------------------

        newServer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {

                newConnection();

            }

            
        });
        
        newChannel.addActionListener(new ActionListener()   {
           public void actionPerformed (ActionEvent ae)   {

                newChannel();

                 
        }
        });

        exitProgram.addActionListener(new ActionListener() { // Closes the program

            public void actionPerformed(ActionEvent evt) {
                try {
                    System.exit(0);
                } catch (SecurityException secE) {
                    System.err.println(I18N.get("mainmenu.error") + secE.getMessage());
                }
            }
        });
    }

    /**
     * Creates the edit-menu
     */
    public void createEditMenu() {

        JMenuItem getChannels = new JMenuItem(I18N.get("mainmenu.getchlist"));
        JMenuItem colors = new JMenuItem(I18N.get("colors.andfonts"));
        JMenuItem changeNick = new JMenuItem(I18N.get("mainmenu.changenick"));
        edit.add(getChannels);
        edit.add(colors);
        edit.add(changeNick);
        
        //--------------Action listeners-----------------------------

        getChannels.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
                getChannels();
               }

            
        });
        

        colors.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent letsMakeSomeColors) {
                getColorChoices();
            }

            
        });
        
        changeNick.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                changeNick();
            }

            
        });

    }

    /**
     * Creates the help-menu
     */
    public void createHelpMenu() {
        final JMenuItem helpItem = new JMenuItem(I18N.get("mainmenu.help"));
        
        help.add(helpItem);
        JMenuItem aboutItem = new JMenuItem(I18N.get("mainmenu.about"));
        help.add(aboutItem);
        CSH.setHelpIDString(helpItem, "top");
            HelpSet hs = getHelpSet("../src/helpfiles/sample.hs");
            HelpBroker hb = hs.createHelpBroker();
            new CSH.DisplayHelpFromSource(hb);

        //--------------Action listeners-----------------------------
     
           helpItem.addActionListener(new CSH.DisplayHelpFromSource(hb));
       //    getHelp();

        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                getAbout();
            }
        });

    } // End of createHelpMenu
    
    public void newChannel() throws HeadlessException {
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
            channelTab.updateStatusScreen("\n"+I18N.get("connection.connectfirst"));
    }
    public void newConnection() {
                // Create and position connection window
                serverConnectWindow scw = new serverConnectWindow();
                scw.setSize(new Dimension(400, 300));
                scw.setLocationRelativeTo(null);
                scw.pack();
                scw.setResizable(false);			// Makes sure user can't change size
                scw.setVisible(true);
    }
    public void getChannels() throws HeadlessException {
                if(connection.connectedToServer()){
                    if(JOptionPane.showConfirmDialog(MainMenu.this, I18N.get("mainmenu.getallchannels") ,I18N.get("mainmenu.getallchannelsheader"),JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/Images/GetAllTheChannels_icon.jpeg")) == JOptionPane.YES_OPTION)
                            connection.getAllTheChannelsFromServer();
                 }
               else
                   channelTab.updateStatusScreen("\n"+I18N.get("connection.connectfirst"));
            }
    public void getColorChoices() {
                Colors.colorWindow();
            }
    public void changeNick() throws HeadlessException {
                if(connection.connectedToServer()){
                String newNick = JOptionPane.showInputDialog(I18N.get("mainmenu.newnick"));
                if (newNick != null)
                    connection.getCurrentSession().changeNick(newNick);
                }
                else
                       channelTab.updateStatusScreen("\n"+I18N.get("connection.connectfirst"));
            }
    public void getHelp() throws HeadlessException {

 JOptionPane.showMessageDialog(channelTab, I18N.get("mainmenu.aboutseljeirc"),
 I18N.get("mainmenu.about"), JOptionPane.PLAIN_MESSAGE, new ImageIcon("src/images/icons/SeiljeIRC_minimal_about.png"));
    
            }
    public void getAbout() throws HeadlessException {
                JOptionPane.showMessageDialog(channelTab, I18N.get("mainmenu.aboutseljeirc"),
                        I18N.get("mainmenu.about"), JOptionPane.PLAIN_MESSAGE);
            }
    public void setTopic(){
        
        
        try{
        JOptionPane jop = new JOptionPane();
        String ch = channelTab.getTitleAt(channelTab.getSelectedIndex());
        String topic = jop.showInputDialog(I18N.get("singletab.whattopic"));
                    channelTab.updateStatusScreen(I18N.get("singletab.updatetopic"));
                    connection.setChannelTopic(ch,topic);
        }catch(Exception e){
            channelTab.updateStatusScreen("Cant set topic unless you are in a channel");
        }
    }

     public HelpSet getHelpSet(String helpsetfile) {
      HelpSet hs = null;
      ClassLoader cl = this.getClass().getClassLoader();
      try {
        URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
        hs = new HelpSet(null, hsURL);
      } catch(Exception ee) {
        System.out.println("HelpSet: "+ee.getMessage());
        System.out.println("HelpSet: "+ helpsetfile + " not found");
      }
      return hs;
   }
        	
}
