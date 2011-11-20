
package SeljeIRC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import jerklib.Channel;
import jerklib.events.modes.ModeAdjustment.Action;

/**
 *
 * @author hallvardwestman
 */
public class ListOfUsers extends JPanel {
    
    DefaultListModel listModel;
    UserListModel lm;
    JList list;
    Channel chan;
    JPopupMenu popup;
    ConnectionHandler connection = SeljeIRC.connection;
    tabHandler tabObject = SeljeIRC.channelTabs;
    
    private Pattern userModePattern = Pattern.compile("^[@|\\+]");		// The regex pattern used to find op and voice
    
    public ListOfUsers(){
        lm = new UserListModel();
        lm.addListDataListener(new ListDataListener()   {

            @Override
            public void intervalAdded(ListDataEvent lde) {
                list.setFixedCellWidth(200);
                list.setVisibleRowCount(lm.getSize());
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
        list.setBackground(Color.GRAY);
        this.add(list);
    }
    
    public void updateList(Channel c)   {
        chan = c;
        SwingUtilities.invokeLater(new Init());
    }
    
    /**
     * Creates the right click popup menu in the user list
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    private void createPopup() {
        popup = new JPopupMenu();
        
        //Main menu items
        JMenuItem whois = new JMenuItem(I18N.get("user.whois"));
        whois.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                connection.getCurrentSession().whois(getSelectedUser());
            }
        });
        JMenuItem query = new JMenuItem(I18N.get("user.query"));
        query.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    openPrivateChat(getSelectedUser());
                } catch (BadLocationException ex) {
                    Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenu control = new JMenu(I18N.get("user.control"));
        JMenu ctcp = new JMenu("CTCP");
        //JMenu dcc = new JMenu("DCC");
        JMenuItem slap = new JMenuItem(I18N.get("user.slap"));
        slap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String action = I18N.get("user.slap.start")+getSelectedUser()+I18N.get("user.slap.end");
                chan.action(action);
                try {
                    tabObject.updateTabScreen(chan.getName(), "* "+connection.getCurrentSession().getNick()+ " "+action);
                } catch (BadLocationException ex) {
                }
            }
        });
        
        //Items for Control sub menu
        JMenuItem op = new JMenuItem("Op");
        op.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.op(getSelectedUser());
            }
            
        });
        JMenuItem deop = new JMenuItem("Deop");
        deop.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.deop(getSelectedUser());
            }
            
        });
        
        JMenuItem voice = new JMenuItem("Voice");
        voice.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.voice(getSelectedUser());
            }
            
        });
        JMenuItem devoice = new JMenuItem("Devoice");
        devoice.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent ae) {
                chan.deVoice(getSelectedUser());
            }
            
        });
        JMenuItem kick = new JMenuItem(I18N.get("user.kick"));
        kick.addActionListener(new KickListener());
        JMenuItem kickWhy = new JMenuItem(I18N.get("user.kickwhy"));
        kickWhy.addActionListener(new KickListener());
        JMenuItem ban = new JMenuItem(I18N.get("user.ban"));
        JMenuItem kickBan = new JMenuItem(I18N.get("user.kickban"));
        
        //Items for CTCP sub menu
        JMenuItem ping = new JMenuItem("Ping");
        ping.addActionListener(new CtcpListener());
        JMenuItem version = new JMenuItem(I18N.get("user.version"));
        version.addActionListener(new CtcpListener());
        JMenuItem time = new JMenuItem(I18N.get("user.time"));
        time.addActionListener(new CtcpListener());
        
        control.add(op);
        control.add(deop);
        control.add(voice);
        control.add(devoice);
        control.add(kick);
        control.add(kickWhy);
        control.add(ban);
        control.add(kickBan);
        
        ctcp.add(ping);
        ctcp.add(version);
        ctcp.add(time);
        
        popup.add(whois);
        popup.add(query);
        popup.addSeparator();
        popup.add(control);
        popup.add(ctcp);
        //popup.add(dcc);
        popup.addSeparator();
        popup.add(slap);
        list.addMouseListener(new MouseEventListener());
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
    	   
    	   
    	   connection.joinChannel(userName);				// Create tab for PM
        }else
        	tabObject.updateStatusScreen("Can't join when not connected"); //TODO: Legg til translation
    }
    
    public UserListModel getListModel()   {
        return lm;
    }
    
    private String getSelectedUser()   {
        String user = (String)list.getSelectedValue().toString();
        user = (user.startsWith("@") || user.startsWith("+")) ? user.substring(1) : user;
        return user;
    }
    
    /**
     * Gets the selected item in the user list
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
        * @author Lars Erik Pedersen
        * @since 0.1
        * @param me event that triggered this function
        */
        @Override
        public void mousePressed(MouseEvent me)   {
           	if(me.getClickCount() == 2) // FIX: Legg til sjekk om den man dobbeltklikker p� er seg selv (CHRISTER)
        		try {
                openPrivateChat(list.getSelectedValue().toString());
            } catch (BadLocationException ex) {
                Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
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
                ((JList)me.getComponent()).setSelectedIndex(getItem(me.getPoint()));
                popup.show(me.getComponent(), me.getX(), me.getY());
            }
        }
        
        
    }
    
    class KickListener implements ActionListener   {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String user = getSelectedUser();
            String myNick = connection.getCurrentSession().getNick();
            if (ae.getActionCommand().equals(I18N.get("user.kickwhy")))   {
                JOptionPane jop = new JOptionPane();
                String why = jop.showInputDialog("Why do you kick?");   //TODO I18N
                if (why == null) return;                                // No input, cancel
                chan.kick(user, why);
            }
            else   {
                chan.kick(user, user);
            }
            if (lm.isOp(myNick))
                lm.removeUser(user);
        }
    }
    
    class CtcpListener implements ActionListener   {

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
                System.out.println("Not able to fetch users yet");
                SwingUtilities.invokeLater(new Init());                 // Try againg later
            }
        }
    }
}
