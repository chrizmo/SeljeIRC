
package SeljeIRC;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.text.BadLocationException;
import jerklib.Channel;


/**
 *  This object is containing all tabs, they are modelled as panels added
 *  directly into the JTabbedPane
 * @author hallvardwestman
 */

public class tabHandler extends JTabbedPane implements FocusListener {
	
    //private static ConnectionHandler connection = SeljeIRC.connection;
    private int curTabIndex = 0;
    private static tabHandler tabHandlerObj = new tabHandler();

    
    
    
    
    /**
     * creates a tabhendler to handle all tabs in jtabbedpane
     * default creates a statustab
     * @throws BadLocationException 
     */
    
    private tabHandler() {//throws BadLocationException{
        super();        
        SingleTab statusTab = new SingleTab("status",this,SingleTab.STATUS);		// Whis the krasjer nå begynner jeg å lure
                              
                
        this.addTab(I18N.get("channeltab.status"), statusTab);
        ButtonTabComponent ctb = new ButtonTabComponent(this,SeljeIRC.connectionHandlerObj,SingleTab.STATUS);
        this.setTabComponentAt(0,ctb);  
        this.remove(0);    
        this.addTab(I18N.get("channeltab.status"), statusTab);

    }
    
	public static synchronized tabHandler getInstance() {
		return tabHandlerObj;
	}
    
    /**
     * Creates new channel-tab or private message-tab
     * @param tabName
     * @param tabType
     * @param topic
     * @throws BadLocationException 
     */
    
    public void createNewTab(String tabName, int tabType, String topic) throws BadLocationException{
        SingleTab st;
        
        /*
         * Creates correct object based on type
         * TODO let statustab be created here?
         */
        
        if(tabType == SingleTab.CHANNEL){
            st = new SingleTab(tabName,this,SingleTab.CHANNEL);
        } else
            st = new SingleTab(tabName,this,SingleTab.PRIVATE);
        
        this.addTab(tabName,st);
        int tabIndex = this.indexOfTab(tabName); 
        
        /*
         * adding closebutton, not on statustab
         */
        
        if(tabType == SingleTab.CHANNEL || tabType == SingleTab.PRIVATE){
            ButtonTabComponent ctb = new ButtonTabComponent(this,SeljeIRC.connectionHandlerObj,tabType);
            this.setTabComponentAt(tabIndex,ctb);
        }
     

        /*
         * setting this tab to be selected
         * TODO should not be done if its privatemessge and user did not
         * create it, should if he did
         */
        
        addFocusListener(this);
        this.setSelectedIndex(this.indexOfTab(tabName));
        
    }
    
    /**
     * You should know what your doing before touching this
     * index of getcomponent needs to be different than setcomponent etc
     * 
     * The array containing all tabs contains a tabcontainer in index 0, and statustab in index 1 etc.
     * this makes it difficult to operate on correct tabs
     * @param channel and message to be displayed
     */
    
    public void updateTabScreen(String ch, String message) throws BadLocationException{
        
        /*
         * getting SingleTab from tabhandler to update screen
         */
        
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.updateScreen(message);
        
        /*
         * checks to see if tab is selected, if not notify user by flagging 
         * this tab in GUI
         */
        
        int curSelected = this.getSelectedIndex();
        final int thisIndex = this.indexOfTab(ch);
        try{
            if(thisIndex != curSelected && thisIndex != 0){
                System.out.print("indexoftab= "+" "+ thisIndex +"curSelected = "+curSelected);
                
                class PrimeThread extends Thread {
                     long minPrime;
                     PrimeThread(long minPrime) {
                         this.minPrime = minPrime;
                     }

                     public void run() {
                        tabHandlerObj.setBackgroundAt(thisIndex, new Color(255,255,0));
                     }
                 }
                
                PrimeThread p = new PrimeThread(143);
                p.start();
                
                
                //Icon icon = new ImageIcon("src/Images/attention-icon.png");
                //this.setIconAt(thisIndex, icon);
                
                
            }
        }catch(Exception e){
            //this is bullshit.

            System.out.print("nonono");
        }
    }
    
    /**
      * use this instead of indexoftab when you want component returned
      * use getIndexOf for title and such
      * @param channel
      */
    
    public int getIndexOfTab(String ch){
            
            return this.indexOfTab(ch)+1;
    }
   
    /**
     * overloaded function for usage of list
     * @author larserik
     * @param channel, list and message to be displayed
     */
    
    void updateTabScreen(String ch, List<String> message) throws BadLocationException {
  
        int tabIndex = this.indexOfTab(ch);
        System.out.printf("updatetab: "+tabIndex);
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.updateScreen(message);

    }
    
    /**
     * updates statustab with param
     * //TODO merge with updateTabScreen
     * @param update to be displayed
     * 
     */
    
    public void updateStatusScreen(String update){
     
        try{
        	updateTabScreen(I18N.get("channeltab.status"),update);					// Bug med at den krasjer med 18N greien
    	}catch(Exception e){
    		System.err.println("System error: " + e.getMessage());
    	}
    }
    
    /**
     * sets connection to param
     * Suspect not in use 
     * //TODO check use
     * @param ch 
     */
    
    public static void setConnection(ConnectionHandler ch){
        //SeljeIRC.connectionHandlerObj = ch; //TODOx,gnfgb
    }
    
    /**
     * Cycles through all tabs except from statustab and removes from jtabbedpane
     * 
     * theTabProblem is affected here
     */
    
    public void removeAllTabs(){
        
        for(int i = 2; i <= this.getTabCount(); i++){
            
            this.remove(i);
            
        }
    }

    // Checks if a certain tab exists
    public boolean tabExists(String ch){
       
    	return((this.indexOfTab(ch) >= 0) ? true : false); //TODO Sjekk om koden kan forberedres
    	
    }
    
    public void fetchUsers(String ch, Channel c)   {
       
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.updateUserList(c);
    }

    void userJoined(String nick, String ch) {
    
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.newUserJoined(nick);
    }

    
    void userLeft(String nick, String ch)   {
        
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.userLeft(nick);
    }
    
    void op(String nick, boolean mode, String ch)   {

        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.op(nick, mode);
    }
    
    void voice(String nick, boolean mode, String ch)   {
      
       SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.voice(nick, mode);
    }
    
    void changedNick(String oldNick, String newNick, String ch)   {
   
       SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.changeNick(oldNick, newNick);
    }
    
    /**
     * Removes notification from tab when selected (focused)
     * sends focus down to inputfield
     * @param fe 
     */
    
    @Override
    public void focusGained(FocusEvent fe) {
        int curSelected = this.getSelectedIndex();
         this.setBackgroundAt(curSelected, Color.black);
         
        SingleTab st = (SingleTab) this.getComponent(curSelected+1);
        st.passFocusToField();
    }

    @Override
    public void focusLost(FocusEvent fe) {
        
    }
    
}
