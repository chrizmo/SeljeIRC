/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author wbserver
 */
public class StatusTab extends JPanel{
    
    private JTextArea screen;
    public ConnectionHandler connection;
    
    public StatusTab(ConnectionHandler ch){
        super();
        connection = ch;
        createStatusTab();
        
    }
    
    public void createStatusTab(){
         /*
         * Borderlayout containing textarea
         */
        BorderLayout bl = new BorderLayout();
        
        /*
         * addTab takes panel, so this panel is modeled as the "tab-object"
         */
        
            setLayout(bl);
            setBackground(Color.darkGray);
        
        /*
         * Textarea containing status
         */
         //TODO set up public object of this that can be reached
         screen = new JTextArea("Welcome to SeljeIRC\n"+"This application could be interpreted as an IRC-client");
            screen.setEditable(false);
            
            screen.setBackground(Color.lightGray);

       
       /*
        * Layout functionality related to statustab
        */
       JScrollPane textAreaScroller = new JScrollPane(screen);
       add(textAreaScroller,BorderLayout.CENTER );
       
       InputField inputField = new InputField(connection,null);
       add(inputField,BorderLayout.SOUTH);

       

    }
    
    public void updateScreen(String update){
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();
      
      screen.append("\n"+dateFormat.format(date) +" " +update);
        // Auto-scroll
      screen.setCaretPosition(screen.getDocument().getLength());
    }
}
