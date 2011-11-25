package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import jerklib.Channel;

/**
 * JPanel for each tab that is created in the JTabbedPane
 * Focuslistener that senses when this "tab" is beeing focused
 * @author Hallvard Westman
 * 
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
    public JLabel topicSetBy;
    JLabel topicFieldHead;
    
    /**
     * Sets all data for this specific tab, aswell as layout.
     * @param ch    //Channel for this "tab"
     * @param ct    // The JTabbedPane so the this "tab" can manipulate its own
     *              place in array, compare to other objects etc
     * @param tabType   //The type of tab 
     * @param topic     //Topic for the specific channel
     */
    public SingleTab(String ch, tabHandler ct, int tabType,Channel channelForTopic){// throws BadLocationException {
        super();
        
        /*
         * Sets incomming params
         */
        typeOfTab = tabType;	
        channel = ch;
        channelTab = ct;    
        index = channelTab.indexOfComponent(this);
        
        /*
         * Borderlayout for specific tab
         * 
         */
        BorderLayout bl = new BorderLayout();
        
        setLayout(bl);
        
        screen = new JTextPane();
        screen.setEditable(false);
        screen.setBackground(Color.white);
        
        
      //  switch(tabType)
        
        /*
         * Checks if this is a channel, in that case it sets the topic
         */
        if(tabType == CHANNEL){
            
            
            /*
             * Setting topic
             * TODO refactor
             */      
             topicField = new JTextPane();
             topicSetBy = new JLabel();
             topicFieldHead = new JLabel();
             
             
             
             try{
                 setTopic(channelForTopic);
             }catch(Exception e){
                 
                 topicField.setText(I18N.get("singletab.settopic"));
             }
             
             /*
              * style
              */
             topicField.setSize(1,1);
             topicField.setBackground(new Color(224,224,224));
             topicField.setEditable(false);
             topicField.setVisible(true);
             
             
             
             JPanel topicPanel = new JPanel();
             BorderLayout tl = new BorderLayout();
             topicPanel.setLayout(tl);
             
             //topicPanel.add(topicFieldHead,BorderLayout.NORTH); //a header that says topic in bold and special color
             topicPanel.add(topicField,BorderLayout.CENTER);
             topicPanel.add(topicSetBy,BorderLayout.EAST);
             add(topicPanel,BorderLayout.NORTH);
             
             
             
             /*
              * setting right side userlist
              */
            listPanel = new ListOfUsers();
            listPanel.setBackground(new Color(215,221,229));
        
            JScrollPane listScroller = new JScrollPane(listPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            listScroller.setPreferredSize(new Dimension(200,2));


            add(listScroller,BorderLayout.EAST);
         }
         
        /*
         * Textarea
         */
        
         
            
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
        inputField = new InputField(channel,this.typeOfTab);
        
        add(textAreaScroller,BorderLayout.CENTER );
        
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
    
    boolean isOp(String nick)   {
        return listPanel.getListModel().isOp(nick);
    }
    
    boolean isVoice(String nick)   {
        return listPanel.getListModel().isVoice(nick);
    }

    
    
    @Override
    public void focusGained(FocusEvent fe) {
       
        System.out.print("mongosingle");
        //inputField.setFocusOnField();
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
    public void setTopic(Channel channelForTopic){
                
        String topic = channelForTopic.getTopic();
        String setBy = channelForTopic.getTopicSetter();
            String[] subSt = setBy.split("~");
            setBy = subSt[0];
        Date setAt = channelForTopic.getTopicSetTime();
           
        topicField.setText(topic);
        topicSetBy.setFont(new Font(Colors.font,Font.PLAIN,11));
        //topicSetBy.setForeground(Color.lightGray);
        
        topicSetBy.setText("Topic was set by "+setBy+" at "+setAt.toString());
        
        //topicFieldHead.setText("Topic");
             
        
    }
    
    
   
}
