package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import jerklib.Channel;

/**
 *
 * @author Hallvard Westman
 */
public class SingleTab extends JPanel implements FocusListener {
	
	final static public int STATUS = 0;
	final static public int CHANNEL = 1;
	final static public int PRIVATE = 2;
	
	
    private String channel;

    private JTextPane screen;
    private ConnectionHandler connection = SeljeIRC.connectionHandlerObj;
    private tabHandler channelTab;
    private int index;
    private ListOfUsers listPanel;
    private int typeOfTab = 1;			// The type of this tab. Standard is channel
    
    private InputField inputField;
    public JTextPane topicField;
    private JButton setTopicButton;
    
    public SingleTab(String ch, tabHandler ct, int tabType,String topic){// throws BadLocationException {
        super();
       
        
        
        typeOfTab = tabType;			// Sets the type of tab
        channel = ch;
        //connection = con;
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
         
         /**
          * Topicfield
          * TODO refactor out?
          */
         
         if(tabType == CHANNEL){
             topicField = new JTextPane();
             if(topic != null)
             topicField.setText(topic);
             else
             topicField.setText(I18N.get("singletab.settopic"));
             topicField.setSize(1,1);
             
             
             topicField.setBackground(new Color(224,224,224));
             topicField.setEditable(false);
             topicField.setVisible(true);
             //add(topicField,BorderLayout.NORTH);
             
             setTopicButton = new JButton(I18N.get("singletab.topic"));
             
             setTopicButton.addActionListener(new ActionListener() {
                JOptionPane jop = new JOptionPane();
                public void actionPerformed(ActionEvent ae) {
                    
                    String topic = jop.showInputDialog(I18N.get("singletab.whattopic"));
                    channelTab.updateStatusScreen(I18N.get("singletab.updatetopic"));
                    connection.setChannelTopic(channel,topic);

                }
             });
             
             JPanel topicPanel = new JPanel();
             BorderLayout tl = new BorderLayout();
             topicPanel.setLayout(tl);
             
             topicPanel.add(topicField,BorderLayout.CENTER);
             topicPanel.add(setTopicButton,BorderLayout.EAST);
             add(topicPanel,BorderLayout.NORTH);
         }
         
        /*
         * Textarea
         */
        
         screen = new JTextPane();
            screen.setEditable(false);
            //screen.setBackground(Color.WHITE);
            /*
             * for alpha
             */
            screen.setBackground(Color.white);
            
        if(tabType == STATUS){
            SimpleAttributeSet color = new SimpleAttributeSet();
            StyleConstants.setFontFamily(color, "Courier New");
            StyleConstants.setForeground(color, Color.black);
            // Print welcome-message in black:
            try{
            	screen.getDocument().insertString(screen.getDocument().getLength(),
                    I18N.get("singletab.welcome"), color);
            // Print message in gray
            	StyleConstants.setForeground(color, Color.gray);
            	screen.getDocument().insertString(screen.getDocument().getLength(),
                    I18N.get("singletab.couldbeirc"), color);
            }catch(BadLocationException ex){
            	System.err.println(I18N.get("connection.systemerror") + ex.getMessage());
            }
        }    
            
            
        JScrollPane textAreaScroller = new JScrollPane(screen);
        add(textAreaScroller,BorderLayout.CENTER );
        
        /*
         * userlist
         */
        if(this.typeOfTab == SingleTab.CHANNEL){
        	listPanel = new ListOfUsers();
            listPanel.setBackground(new Color(215,221,229));
        
            
        /*
         * layout functionality for tabs
         */
            JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        	listScroller.setPreferredSize(new Dimension(200,2));

        	
        	add(listScroller,BorderLayout.EAST);
        	
        	
        } 
        inputField = new InputField(channel,this.typeOfTab);
        add(inputField,BorderLayout.SOUTH);

        passFocusToField();
        
        
    }
    
    /** Returns the type of the current tab
     * @author Hallvard Westman
     * @return typeOfTab The type of tab
     */
    public int getType(){
        return typeOfTab;
    }

    /**
     * @author Hallvard Westman
     * @author Jon Arne Westgaard
     * @param update The text to print
     * @param theColor Color of the text
     * @throws BadLocationException
     */
    void updateScreen(String update, Color theColor) throws BadLocationException {
      SimpleAttributeSet color = new SimpleAttributeSet();
      StyleConstants.setFontFamily(color, Colors.font);
      StyleConstants.setForeground(color, theColor);
      StyleConstants.setFontSize(color, Colors.fontSize);
      screen.getDocument().insertString(screen.getDocument().getLength()," " +update, color);
      screen.setCaretPosition(screen.getDocument().getLength());

    }

    /**
     * @author Hallvard Westman
     * @author Jon Arne Westgaard
     * @param update The text to print
     * @param theColor Color of the text
     * @throws BadLocationException
     */
    void updateScreen(List<String> update, Color theColor) throws BadLocationException {
      SimpleAttributeSet color = new SimpleAttributeSet();
      StyleConstants.setFontFamily(color, Colors.font);
      StyleConstants.setForeground(color, theColor);
      screen.getDocument().insertString(screen.getDocument().getLength(),
                    " " +update, color);
      screen.setCaretPosition(screen.getDocument().getLength());

    }
    
    /**
     * Updates userlist of a channel tab
     * @param c Channel to get updates from
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void updateUserList(Channel c) {
        listPanel.updateList(c);
    }
    
    /**
     * Adds a newly joined user to the userlist
     * @param n Nick name of joined user
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void newUserJoined(String n)   {
        listPanel.getListModel().addUserToList(n);
    }
    
    /**
     * Removes a parted user from the userlist
     * @param n Nick name of parted user
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void userLeft(String n)   {
        listPanel.getListModel().removeUser(n);
    }
    
    /**
     * Reflects a +o or -o mode change in the userlist
     * @param n Nick name of "victim"
     * @param mode True = given op, false = given deop
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void op(String n, boolean mode)   {
        listPanel.getListModel().op(n, mode);
    }
    
    /**
     * Reflects a +v or -v mode change in the userlist
     * @param n Nick name of "victim"
     * @param mode  True = given voice, false = given devoice
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void voice(String n, boolean mode)   {
        listPanel.getListModel().voice(n, mode);
    }
    
    /**
     * Pass a nick change to the userlist
     * @param oldNick User's old nick
     * @param newNick User's new nick
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    void changeNick(String oldNick, String newNick)   {
        listPanel.getListModel().changeNick(oldNick, newNick);
    }

    
    
    @Override
    public void focusGained(FocusEvent fe) {
       inputField.setFocusOnField();
    }

    @Override
    public void focusLost(FocusEvent fe) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    public void passFocusToField(){
       inputField.setFocusOnField(); 
    }

    /**
     * Sets topic
     * @param topic The topic to set
     */
    public void setTopic(String topic){
        topicField.setText(topic);
    }
    
    
   
}
