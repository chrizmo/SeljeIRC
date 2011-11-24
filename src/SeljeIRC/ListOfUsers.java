
package SeljeIRC;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import jerklib.Channel;
import jerklib.events.modes.ModeAdjustment.Action;

/**
 * Makes the panel that contains the nick list of every channel tab.
 * @author Hallvard Westman
 * @author Christer Vaskinn
 * @author Lars Erik Pedersen
 */
public class ListOfUsers extends JPanel {
    
    DefaultListModel listModel;
    UserListModel lm;
    JList list;
    Channel chan;
    JPopupMenu popup;
    ConnectionHandler connection = SeljeIRC.connectionHandlerObj;
    tabHandler tabObject = SeljeIRC.channelTabObj;
    
    private Pattern userModePattern = Pattern.compile("^[@|\\+]");		// The regex pattern used to find op and voice
    
    /**
     * Constructor, creats layout, sets listmodel and renderer
     * @author Hallvard Westman
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    public ListOfUsers(){
        lm = new UserListModel();
        lm.addListDataListener(new ListDataListener()   {                       // Listens for changes is list, makes sure the size fits content

            @Override
            public void intervalAdded(ListDataEvent lde) {
                list.setFixedCellWidth(200);
                list.setVisibleRowCount(lm.getSize());                          // List is allways as high as the amount of users in it
            }

            @Override
            public void intervalRemoved(ListDataEvent lde) {
                list.setFixedCellWidth(200);
                list.setVisibleRowCount(lm.getSize());
            }

            @Override
            public void contentsChanged(ListDataEvent lde) {
                list.setFixedCellWidth(200);
                list.setVisibleRowCount(lm.getSize());
            }
            
        });
        list = new JList(lm);
        list.setCellRenderer(new UserListRenderer());
        list.setLayoutOrientation(JList.VERTICAL);
        list.setFixedCellWidth(200);
        createPopup();
        list.setBackground(new Color(215,221,229));
        this.add(list);
    }
    
    /**
     * Does the first initialization of the user list, when it's recived from the channel
     * @param c Channel to fetch users from
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    public void updateList(Channel c)   {
        chan = c;
        SwingUtilities.invokeLater(new Init());         // Run a new thread for init
    }
    
    /**
     * Creates the right click popup menu in the user list
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    private void createPopup() {
        popup = new JPopupMenu();
        
//--------------------------------------Main menu items----------------------------------
        JMenuItem whois = new JMenuItem(I18N.get("user.whois"));        // Perform whois on selected user
        JMenuItem query = new JMenuItem(I18N.get("user.query"));        // Opens a private chat
        
        JMenu control = new JMenu(I18N.get("user.control"));            // Actions for channel operators
        JMenuItem op = new JMenuItem("Op");                                 // Make a user channel operator
        JMenuItem deop = new JMenuItem("Deop");                             // Take operator rights away
        JMenuItem voice = new JMenuItem("Voice");                           // Voice user
        JMenuItem devoice = new JMenuItem("Devoice");                       // Devoice user
        JMenuItem kick = new JMenuItem(I18N.get("user.kick"));              // Kick this user
        JMenuItem kickWhy = new JMenuItem(I18N.get("user.kickwhy"));        // Kick, and tell him why
        JMenuItem ban = new JMenuItem(I18N.get("user.ban"));                // Ban this user
        JMenuItem kickBan = new JMenuItem(I18N.get("user.kickban"));        // Ban and kick him
           
        JMenu ctcp = new JMenu("CTCP");                                 // Various CTCP commands
        JMenuItem ping = new JMenuItem("Ping");                             // Ping
        JMenuItem version = new JMenuItem(I18N.get("user.version"));        // Get version of users IRC client
        JMenuItem time = new JMenuItem(I18N.get("user.time"));              // Get system time from user
        JMenuItem slap = new JMenuItem(I18N.get("user.slap"));          // Sends a /me action with a hardcoded text 
        
// ------------------------------------Action Listeneres for main menu items-------------
        whois.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                connection.getCurrentSession().whois(getSelectedUser());        // Sends whois
            }
        });
        
        query.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    openPrivateChat(getSelectedUser());                         // Open private chat
                } catch (BadLocationException ex) {
                }
            }
        });
        
        slap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String action = I18N.get("user.slap.start")+getSelectedUser()+I18N.get("user.slap.end");        // /me action to send
                String myNick = getMyNick();
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("HH:mm");
                chan.action(action);                                                                            // send it
                try {
                    tabObject.updateTabScreen(chan.getName(), "\n"+df.format(date)+" * "+myNick+ " "+action);   // print it in channel window
                } catch (BadLocationException ex) {
                }
            }
        });
        
//----------------------------------------Control sub menu items-------------------------
        
        op.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.op(getSelectedUser());                     // Send mode change to channel
            }
            
        });
        
        deop.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.deop(getSelectedUser());
            }
            
        });
        
        
        voice.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.voice(getSelectedUser());
            }
            
        });
        
        devoice.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.deVoice(getSelectedUser());
            }
            
        });
        
        kick.addActionListener(new KickListener());     // Uses a inner class, since they are quite similar
        kickWhy.addActionListener(new KickListener());
        
        
//------------------------------------CTCP sub menu items--------------------------------
        
        ping.addActionListener(new CtcpListener());         // Uses a inner class, since they are quite similar
        version.addActionListener(new CtcpListener());
        time.addActionListener(new CtcpListener());
        
        // Add all items of control sub menu
        control.add(op);
        control.add(deop);
        control.add(voice);
        control.add(devoice);
        control.add(kick);
        control.add(kickWhy);
        control.add(ban);
        control.add(kickBan);
        
        // Add all items of CTCP sub menu
        ctcp.add(ping);
        ctcp.add(version);
        ctcp.add(time);
        
        // Add all submenus and main menu items
        popup.add(whois);
        popup.add(query);
        popup.addSeparator();
        popup.add(control);
        popup.add(ctcp);
        popup.addSeparator();
        popup.add(slap);
        list.addMouseListener(new MouseEventListener());        // Add mouse listener
    }
    
    /**
     * Opens a private chat window with a user
     * @author Christer Vaskinn
     * @since 0.1
     * @param userName The username to which the user wants to have a private conversation with
     * 
     */
    private void openPrivateChat(String userName) throws BadLocationException{
       if(connection.connectedToServer()){
    	   Matcher userModeMatcher = userModePattern.matcher(userName);		// Used for regex evaluation
    	   if(userModeMatcher.find())										// Checks for op or voice in username
    		   userName = userName.substring(1);							// Removes the symbol in front of username
    	   
    	   if(!tabObject.tabExists(userName)){
                tabObject.createNewTab(userName,SingleTab.PRIVATE,null);				// Create tab for PM
           }
           else
               tabObject.setSelectedIndex(tabObject.indexOfTab(userName));
        }else
        	tabObject.updateStatusScreen(I18N.get("user.notconnected")); //TODO: Legg til translation
    }
    
    private String getMyNick()   {
        return connection.getCurrentSession().getNick();
    }
    
    /**
     * Fetches the listmodel of the list
     * @return The list model
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    public UserListModel getListModel()   {
        return lm;
    }
    
    /**
     * Gets nickname of the selected user in the nick list
     * @return The nickname
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    private String getSelectedUser()   {
        String user = (String)list.getSelectedValue().toString();
        user = (user.startsWith("@") || user.startsWith("+")) ? user.substring(1) : user;
        return user;
    }
    
    /**
     * Gets the selected item in the user list, from where you actually clicked
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param p The selected point on screen
     * @return Index of the selected item
     */
    private int getItem(Point p)   {
        return list.locationToIndex(p);
    }
    
    /**
     * Inner class. Listens for mouse events in the user list
     * @author Lars Erik Pedersen
     * @version 0.1
     * @since 0.1
     */
    class MouseEventListener extends MouseAdapter   {
        
        /**
        * Listens for mouse pressed
        * @author Christer Vaskinn
        * @since 0.1
        * @param me event that triggered this function
        */
        @Override
        public void mousePressed(MouseEvent me)   {
            if(me.getClickCount() == 2) // FIX: Legg til sjekk om den man dobbeltklikker p� er seg selv (CHRISTER)
     		try {
                   openPrivateChat(list.getSelectedValue().toString());
                } catch (BadLocationException ex) {
                }
           else	
             Popup(me);
        }
        
        /**
        * Listens for mouse released
        * @author Lars Erik Pedersen
        * @since 0.1
        * @param me event that triggered this function
        */
        @Override
        public void mouseReleased(MouseEvent me)   {
            Popup(me);
        }
        
        /**
        * Shows popup menu when right clicking in the user list.
        * @author Lars Erik Pedersen
        * @since 0.1
        * @param me event that triggered this function
        */
        private void Popup(MouseEvent me)   {
            if(me.isPopupTrigger())   {
                ((JList)me.getComponent()).setSelectedIndex(getItem(me.getPoint()));    // Ensure the item clicked is paintes as selected
                popup.show(me.getComponent(), me.getX(), me.getY());                    // Show popup menu where you clicked
            }
        }

    }
    
    /**
     * Inner class, listens for actions on the kick, and kickwhy menu items
     * @author Lars Erik Pedersen
     * @version 0.1
     * @since 0.1
     */
    class KickListener implements ActionListener   {
        
        /**
         * Method called when you click on a menu item
         * @param ae Event that triggered function
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String user = getSelectedUser();
            String myNick = getMyNick();
            if (ae.getActionCommand().equals(I18N.get("user.kickwhy")))   {     // Get what menu item was clicked
                JOptionPane jop = new JOptionPane();                            // Will tell why user is kicked
                String why = jop.showInputDialog(I18N.get("user.whykick"));           //TODO I18N
                if (why == null) return;                                        // No input, cancel
                    chan.kick(user, why);
            }
            else   {                                                            // No reason, sends nick as reason
                chan.kick(user, user);
            }
            if (lm.isOp(myNick))                                                // Only remove victim from nick list, if you are a operator
                lm.removeUser(user);
        }
    }
    
    /**
     * Inner class, listens for actions in the CTCP sub menu
     * @author Lars Erik Pedersen
     * @version 0.1
     * @since 0.1
     */
    class CtcpListener implements ActionListener   {
        
        /**
         * Method called when you click on a menu item
         * @param ae Event that triggered function
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String user = getSelectedUser();
            if (ae.getActionCommand().equals(I18N.get("user.version")))
                connection.getCurrentSession().ctcp(user, "VERSION");
            else if (ae.getActionCommand().equals("Ping"))
                connection.getCurrentSession().ctcp(user, "PING");
            else if (ae.getActionCommand().equals(I18N.get("user.time")))
                connection.getCurrentSession().ctcp(user, "TIME");
        }
        
    }
   
    
    /**
     * Fetches the user list from given channel.
     * The fetching of users needs to be a thread, because otherwise it will lock the GUI when trying to fetch
     * @author Lars Erik Pedersen
     * @version 0.1
     * @since 0.1
     */
    class Init implements Runnable   {
        
        /**
         * Fetches regular users, ops and voices from channel
         * @author Lars Erik Pedersen
         * @since 0.1
         */
        @Override
        public void run() {
            try   {
                if (!chan.getNicks().iterator().hasNext())              // Not able to fetch users
                    SwingUtilities.invokeLater(new Init());             // Try again
                Iterator i = chan.getNicks().iterator();                // Get iterator
                lm.removeAllElements();
                while (i.hasNext())   {                                 // More users
                    lm.addUserToList((String)i.next());                 // Add them to list
                }
                i = chan.getNicksForMode(Action.PLUS, 'o').iterator();  // Get ops
                while (i.hasNext())   {                                 // More ops
                    lm.op((String)i.next(), true);                      // Op those users
                }
                i = chan.getNicksForMode(Action.PLUS, 'v').iterator();  // Get voices
                while (i.hasNext())   {                                 // More voices
                    lm.voice((String)i.next(), true);                   // Voice those users
                }
            }
            catch (Exception e)   {             // Some exception..
                System.out.println(I18N.get("user.notusersyet"));
                SwingUtilities.invokeLater(new Init());                 // Try againg later
            }
        }
    }
}
