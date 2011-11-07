/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import jerklib.Channel;
import jerklib.events.modes.ModeAdjustment.Action;

/**
 *
 * @author hallvardwestman
 */
public class ListOfUsers extends JPanel{
    
    DefaultListModel listModel;
    UserListModel lm;
    JList list;
    Channel chan;
    
    
    public ListOfUsers(){

        listModel = new DefaultListModel();
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        
        list = new JList(listModel);
        
        
        list.setLayoutOrientation(JList.VERTICAL);
        
        Dimension d = list.getPreferredSize();
        int height =(int)d.getHeight();
        list.setPreferredSize(new Dimension(200,height));
        list.setBackground(Color.GRAY);
        
        this.add(list); 
    }
    
    public ListOfUsers(Channel c)   {
        chan = c;
        lm = new UserListModel();
        list = new JList(lm);
        list.setLayoutOrientation(JList.VERTICAL);
        SwingUtilities.invokeLater(new Init());
        Dimension d = list.getPreferredSize();
        int height =(int)d.getHeight();
        list.setPreferredSize(new Dimension(200,height));
        list.setBackground(Color.GRAY);
        
        this.add(list);
        
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
         * Fetches regular users, ops and voices from channe
         * @author Lars Erik Pedersen
         * @since 0.1
         */
        @Override
        public void run() {
            try   {
                if (!chan.getNicks().iterator().hasNext())              // Not able to fetch users
                    SwingUtilities.invokeLater(new Init());             // Try again
                Iterator i = chan.getNicks().iterator();                // Get iterator
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
            catch (Exception e)   {                                     // Some exception...
                SwingUtilities.invokeLater(new Init());                 // Try againg later
            }
        }
    }
}
