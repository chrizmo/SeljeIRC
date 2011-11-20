/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
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

    private JTextPane screen;
    private ConnectionHandler connection;
    private tabHandler channelTab;
    private int index;
    private ListOfUsers listPanel;
    private int typeOfTab = 1;			// The type of this tab. Standard is channel
    
    
    public SingleTab(ConnectionHandler con,String ch, tabHandler ct, int tabType) throws BadLocationException{
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
        screen = new JTextPane();
            screen.setEditable(false);
            screen.setBackground(Color.lightGray);
            
        if(tabType == STATUS){
            SimpleAttributeSet color = new SimpleAttributeSet();
            StyleConstants.setFontFamily(color, "Courier New");
            StyleConstants.setForeground(color, Color.black);
            // Print welcome-message in black:
            screen.getDocument().insertString(screen.getDocument().getLength(),
                    "Welcome to SeljeIRC\n", color);
            // Print message in gray
            StyleConstants.setForeground(color, Color.gray);
            screen.getDocument().insertString(screen.getDocument().getLength(),
                    "This application could be interpreted as an IRC-client", color);
        }    
            
            
        JScrollPane textAreaScroller = new JScrollPane(screen);
        add(textAreaScroller,BorderLayout.CENTER );
        
        /*
         * userlist
         */
        //TODO set up the userlist model
        if(this.typeOfTab == SingleTab.CHANNEL){
        	listPanel = new ListOfUsers();
            listPanel.setBackground(Color.GRAY);
        
            
        /*
         * layout functionality for tabs
         */
            JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        	listScroller.setPreferredSize(new Dimension(200,2));

        	
        	add(listScroller,BorderLayout.EAST);
        	
        	
        } 
        InputField inputField = new InputField(connection,channel,this.typeOfTab);
        add(inputField,BorderLayout.SOUTH);

        
        
        
    }
    public int getType(){
        return typeOfTab;
    }
    public void updateScreen(String update) throws BadLocationException{
      // TODO This is ugly, but will be fixed when Colors is done
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();

      SimpleAttributeSet color = new SimpleAttributeSet();
      StyleConstants.setFontFamily(color, "Courier New");
      StyleConstants.setForeground(color, Color.black);
      screen.getDocument().insertString(screen.getDocument().getLength(),
                    "\n"+dateFormat.format(date) +" " +update, color);
      screen.setCaretPosition(screen.getDocument().getLength());
      
      
          
      
    }

    void updateScreen(List<String> update) throws BadLocationException {
      // TODO This is ugly, but will be fixed when Colors is done
      DateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Date date = new Date();   

      SimpleAttributeSet color = new SimpleAttributeSet();
      StyleConstants.setFontFamily(color, "Courier New");
      StyleConstants.setForeground(color, Color.black);
      screen.getDocument().insertString(screen.getDocument().getLength(),
                    "\n"+dateFormat.format(date) +" " +update, color);
      screen.setCaretPosition(screen.getDocument().getLength());

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
    
    void changeNick(String oldNick, String newNick)   {
        listPanel.getListModel().changeNick(oldNick, newNick);
    }
    
   
}
