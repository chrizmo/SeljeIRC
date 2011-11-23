/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author wbserver
 */
public class StatusTab extends JPanel{
    
    private JTextPane screen;
    public ConnectionHandler connection;
    private String buttonString;    
    public StatusTab(ConnectionHandler ch) throws BadLocationException{
        // TODO Do we really need to throw things around??
        super();
        connection = ch;
        createStatusTab();
        
    }
    
    public void createStatusTab() throws BadLocationException{
        // TODO Do we really need to throw things around??
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
         // TODO set up public object of this that can be reached
         // TODO Line 62-64 could probably be moved out of this function... But I'm tired... And lazy.
        // TODO Remove line 62-64 and get standard color from Colors.java
            screen = new JTextPane();
            screen.setEditable(false);
            screen.setBackground(Color.lightGray);
            // TODO This is ugly, but will be fixed when Colors is done:
            SimpleAttributeSet color = new SimpleAttributeSet();
            StyleConstants.setFontFamily(color, "Courier New");
            StyleConstants.setForeground(color, Color.black);
            // Print welcome-message in black:
            screen.getDocument().insertString(screen.getDocument().getLength(),
                    "Welcome to SeljeIRC\n", color);
            // Print message in gray
            StyleConstants.setForeground(color, Color.gray);
            
            screen.getDocument().insertString(screen.getDocument().getLength(),
                    "This application could be interpreted as an IRC-clientT "+color, color);
            screen.getDocument().insertString(screen.getDocument().getLength(),"hei "+color, color);

            
            
            
       /*
        * Layout functionality related to statustab
        */
       JScrollPane textAreaScroller = new JScrollPane(screen);
       add(textAreaScroller,BorderLayout.CENTER );
       
       InputField inputField = new InputField(connection,null,SingleTab.STATUS);
       add(inputField,BorderLayout.SOUTH);

       

    }
    
    public void updateScreen(String update) throws BadLocationException{
      // TODO Do we really need to throw things around?
      // Some stuff to define the colors
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();

      SimpleAttributeSet textColor = new SimpleAttributeSet();
      StyleConstants.setFontFamily(textColor, Colors.font);
      StyleConstants.setForeground(textColor, Colors.statusColor);
      


      //screen.append("\n"+dateFormat.format(date) +" " +update);
      screen.getDocument().insertString(screen.getDocument().getLength() + 1,
                    "\n" + dateFormat.format(date) + " " + update, textColor);

      // Auto-scroll
      screen.setCaretPosition(screen.getDocument().getLength());
    }
}
