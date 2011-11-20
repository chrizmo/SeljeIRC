
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
//TODO 
public class tabHandler extends JTabbedPane implements FocusListener {
	
    /*
     * indexing all tabs 
     */
    private static ConnectionHandler connection;
     
    
    
    
    private int curTabIndex = 0;
   
    
     
    public tabHandler() throws BadLocationException{
        super();
        /* 
         * what im doing?
         */   
        SingleTab statusTab = new SingleTab(connection,"status",this,SingleTab.STATUS);
                
 //Object o = this.getComponents();               
                
        this.addTab(I18N.get("channeltab.status"), statusTab);
// o = this.getComponents();       
        ButtonTabComponent ctb = new ButtonTabComponent(this,connection,SingleTab.STATUS);
        this.setTabComponentAt(0,ctb);
 //o = this.getComponents();       
        this.remove(0);
 //o = this.getComponents();       
        this.addTab(I18N.get("channeltab.status"), statusTab);
    }
    
    /*
     * only the statustab is created here, containing just jtextarea
     */
  
    
    /**
     * add tabs for each channel
     * @throws BadLocationException 
     */
    
    
    /**
     * 
     * 
     */
    public void createNewTab(String tabName, int tabType,String topic) throws BadLocationException{
        SingleTab st;
       
        
                    if(tabType == SingleTab.CHANNEL)			// Checks the tab type
    				st = new SingleTab(connection,tabName,this,SingleTab.CHANNEL);
    			else
    				st = new SingleTab(connection,tabName,this,SingleTab.PRIVATE);
                                
                        
                    
                    addFocusListener(this);
                        
                        this.addTab(tabName,st);
                        int tabIndex = this.indexOfTab(tabName); 
                        /*
                         * adding closebutton, not on statustab
                         */
                        if(tabType == SingleTab.CHANNEL || tabType == SingleTab.PRIVATE){
                            ButtonTabComponent ctb = new ButtonTabComponent(this,connection,tabType);
                            this.setTabComponentAt(tabIndex,ctb);
                        }
                            //checking whats in the jtabbedpane
                    this.setSelectedIndex(tabIndex);
                        this.updateTabScreen(tabName,topic);
      
}
    public void updateTabScreen(String ch, String message) throws BadLocationException{
            
            //System.out.print(message);
            SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
                st.updateScreen(message);
            
            
            int curSelected = this.getSelectedIndex();
            int thisIndex = this.indexOfTab(ch);
            
            try{
                if(thisIndex != curSelected && thisIndex != 0){
                    System.out.print("indexoftab= "+" "+thisIndex + "curSelected = "+curSelected );

                    this.setBackgroundAt(thisIndex, Color.blue);

                }
            }catch(Exception e){
                //this is bullshit.
            }
   
    }
    /*
     * use this instead of indexoftab when you want the right component out and not just title
     */
    public int getIndexOfTab(String ch){
            
            return this.indexOfTab(ch)+1;
    }
   
    /*
     * function overloaded
     */
    void updateTabScreen(String ch, List<String> message) throws BadLocationException {
  
        int tabIndex = this.indexOfTab(ch);
        System.out.printf("updatetab: "+tabIndex);
        SingleTab st = (SingleTab) this.getComponent(this.getIndexOfTab(ch));
        st.updateScreen(message);

    }
    
    public void updateStatusScreen(String ch){
     
        try{
        	updateTabScreen("status",ch);
    	}catch(Exception e){
    		System.err.println("System error " + e.getMessage());
    	}
    }
    
    public static void setConnection(ConnectionHandler ch){
        connection = ch;
    }
    
    public void removeAllTabs(){
        
        for(int i = 1; i <= this.getTabCount(); i++){
            
            this.remove(i);
            
        }
    }

    // Checks if a certain tab exists
    public boolean tabExists(String ch){
       
    	return((this.indexOfTab(ch) >= 0) ? true : false); //FIX: Sjekk om koden kan forberedres
    	
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

    @Override
    public void focusGained(FocusEvent fe) {
        int curSelected = this.getSelectedIndex();
         this.setBackgroundAt(curSelected, Color.GRAY);
        
    }

    @Override
    public void focusLost(FocusEvent fe) {
        
    }
}
