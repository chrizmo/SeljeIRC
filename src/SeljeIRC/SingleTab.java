/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author wbserver
 */
public class SingleTab extends JPanel {
    
    private String channel;
    private JTextArea screen;
    private ConnectionHandler connection;
    
    public SingleTab(ConnectionHandler con,String ch){
        super();
        channel = ch;
        connection = con;
        
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
        ListOfUsers listPanel = new ListOfUsers();
            listPanel.setBackground(Color.GRAY);
        
            
        /*
         * layout functionality for tabs
         */
        JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(textAreaScroller,BorderLayout.CENTER );
        add(listScroller,BorderLayout.EAST);
        
        InputField inputField = new InputField(connection,channel);
        add(inputField,BorderLayout.SOUTH);
       
        
        
    }
    public void updateScreen(String update){
        screen.append("\n"+update);
    }
   
}
