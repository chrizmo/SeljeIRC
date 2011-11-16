/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import jerklib.Channel;

/**
 *
 * @author wbserver
 */
public class SingleTab extends JPanel{
	
	final static public int STATUS = 0;
	final static public int CHANNEL = 1;
	final static public int PRIVATE = 2;
	
	
    private String channel;
    private JTextArea screen;
    private ConnectionHandler connection;
    private tabHandler channelTab;
    private int index;
    private ListOfUsers listPanel;
    private int typeOfTab = 1;			// The type of this tab. Standard is channel
    
    
    public SingleTab(ConnectionHandler con,String ch, tabHandler ct, int tabType){
        super();
        
        typeOfTab = tabType;			// Sets the type of tab
        channel = ch;
        connection = con;
        channelTab = ct;
        index = channelTab.indexOfComponent(this);
        
        /*
         * Borderlayout containing textarea and userlist
         * 
         */
        BorderLayout bl = new BorderLayout();
        
        /*
         * addTab takes panel, so this panel is modeled as the "tab-object"
         *
         */
      
         setLayout(bl);
         setBackground(Color.darkGray);
            
        /*
         * Textarea
         */
        screen = new JTextArea();
            screen.setEditable(false);
            
            screen.setBackground(Color.lightGray);
            
        JScrollPane textAreaScroller = new JScrollPane(screen);
        
        
        /*
         * userlist
         */
        //TODO set up the userlist model
        
        listPanel = new ListOfUsers();
        listPanel.setBackground(Color.GRAY);
        
            
        /*
         * layout functionality for tabs
         */
        JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        listScroller.setPreferredSize(new Dimension(200,2));

        add(textAreaScroller,BorderLayout.CENTER );
        add(listScroller,BorderLayout.EAST);
        
        InputField inputField = new InputField(connection,channel,this.typeOfTab);
        add(inputField,BorderLayout.SOUTH);

        
        
        
    }
    public void updateScreen(String update){
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();
      screen.append("\n"+dateFormat.format(date) +" " +update);
    }

    void updateScreen(List<String> update) {
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();   
      screen.append("\n"+dateFormat.format(date) +" " +update);

    }
    void updateUserList(Channel c) {
        listPanel.updateList(c);
    }
    
    void newUserJoined(String n)   {
        listPanel.getListModel().addUserToList(n);
    }
    
    void userLeft(String n)   {
        listPanel.getListModel().removeUser(n);
    }
    
    void op(String n, boolean mode)   {
        listPanel.getListModel().op(n, mode);
    }
    
    void voice(String n, boolean mode)   {
        listPanel.getListModel().voice(n, mode);
    }
   
}
