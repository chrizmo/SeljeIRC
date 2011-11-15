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
public class SingleTab extends JPanel implements ActionListener {
    
    private String channel;
    private JTextArea screen;
    private ConnectionHandler connection;
    private ChannelTab channelTab;
    private int index;
    private ListOfUsers listPanel;
    
    public SingleTab(ConnectionHandler con,String ch, ChannelTab ct){
        super();
        channel = ch;
        connection = con;
        channelTab = ct;
        index = channelTab.indexOfComponent(this);
        
        setOpaque(false);
        JButton btClose = new JButton("x");
            btClose.setPreferredSize(new Dimension(10,10));
            add(btClose);
        btClose.addActionListener(this);  
        
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
        
        InputField inputField = new InputField(connection,channel);
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
    
    public void actionPerformed(ActionEvent e) {
            
            if (index != -1) {
                connection.disconnectFromChannel(channel);
                channelTab.remove(index);
              
            }
          }

    void updateUserList(Channel c) {
        listPanel.updateList(c);
    }
    
    
   
}
