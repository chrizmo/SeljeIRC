
package SeljeIRC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
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
    ChannelTab tabObject = SeljeIRC.channelTabs;
    
    
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
        JMenuItem query = new JMenuItem(I18N.get("user.query"));
        JMenu control = new JMenu(I18N.get("user.control"));
        JMenu ctcp = new JMenu("CTCP");
        //JMenu dcc = new JMenu("DCC");
        JMenuItem slap = new JMenuItem(I18N.get("user.slap"));
        
        //Items for Control sub menu
        JMenuItem op = new JMenuItem("Op");
        JMenuItem deop = new JMenuItem("Deop");
        JMenuItem voice = new JMenuItem("Voice");
        JMenuItem devoice = new JMenuItem("Devoice");
        JMenuItem kick = new JMenuItem(I18N.get("user.kick"));
        JMenuItem ban = new JMenuItem(I18N.get("user.ban"));
        JMenuItem kickban = new JMenuItem(I18N.get("user.kickban"));
        
        //Items for CTCP sub menu
        JMenuItem ping = new JMenuItem("Ping");
        JMenuItem version = new JMenuItem(I18N.get("user.version"));
        
        control.add(op);
        control.add(deop);
        control.add(voice);
        control.add(devoice);
        control.add(kick);
        control.add(ban);
        control.add(kickban);
        
        ctcp.add(ping);
        ctcp.add(version);
        
        popup.add(whois);
        popup.add(query);
        popup.addSeparator();
        popup.add(control);
        popup.add(ctcp);
        //popup.add(dcc);
        popup.addSeparator();
        popup.add(slap);
        list.addMouseListener(new PopupListener());
        list.addMouseListener(new DoubleClickListener());
    }
    /**
     * Opens a private chat window with a user
     * @author Christer Vaskinn
     * @since 0.1
     * @param userName The username to which the user wants to have a private conversation with
     * 
     */
    private void openPrivateChat(String userName){
       if(connection.connectedToServer()){
        	tabObject.createNewTab(userName,SingleTab.PRIVATE);
        }else
        	tabObject.updateStatusScreen("Can't join when not connected"); //TODO: Legg til translation
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
     * Listens for double click to invoce private chat
     * @author Christer Vaskinn
     * @since 0.1
     */
    private class DoubleClickListener extends MouseAdapter{
    	
    	
    	public void mousePressed(MouseEvent evt){
           	if(evt.getClickCount() == 2) // FIX: Legg til sjekk om den man dobbeltklikker på er seg selv (CHRISTER)
        		openPrivateChat(list.getSelectedValue().toString());

    	}
    }
    
    /**
     * Inner class. Listens for mouse events in the user list
     * @author Lars Erik Pedersen
     * @version 0.1
     * @since 0.1
     */
    class PopupListener extends MouseAdapter   {
        
        /**
        * Listens for mouse pressed
        * @author Lars Erik Pedersen
        * @since 0.1
        * @param me event that triggered this function
        */
        @Override
        public void mousePressed(MouseEvent me)   {
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
            catch (Exception e)   {             // Some exception...
                e.printStackTrace();
                SwingUtilities.invokeLater(new Init());                 // Try againg later
            }
        }
    }
}
