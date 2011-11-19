
package SeljeIRC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import jerklib.Channel;

/**
 *  This object is containing all tabs, they are modelled as panels added
 *  directly into the JTabbedPane
 * @author hallvardwestman
 */
//TODO 
public class tabHandler extends JTabbedPane {
	
    /*
     * indexing all tabs 
     */
    private static ConnectionHandler connection;
    StatusTab statusTab;
    
    
    
    private int curTabIndex = 0;
   
    
     
    public tabHandler() throws BadLocationException{
        super();
        /* 
         * what im doing?
         */   
        
        
//        ButtonTabComponent ctb = new ButtonTabComponent(this,connection);
      
        
        
        this.createStatusTab();
    }
    
    /**
     * only the statustab is created here, containing just jtextarea
     */
  
    
    /**
     * add tabs for each channel
     * @throws BadLocationException 
     */
    
    public void createStatusTab() throws BadLocationException{
        try{
        	statusTab = new StatusTab(connection);
                
 //Object o = this.getComponents();               
                
        this.addTab(I18N.get("channeltab.status"), statusTab);
// o = this.getComponents();       
        ButtonTabComponent ctb = new ButtonTabComponent(this,connection);
        this.setTabComponentAt(0,ctb);
 //o = this.getComponents();       
        this.remove(0);
 //o = this.getComponents();       
        this.addTab(I18N.get("channeltab.status"), statusTab);
     
        
        /*
  o = this.getComponents();
  
  this.addTab("a",new JTextArea("aaa"));
    ButtonTabComponent aa = new ButtonTabComponent(this,connection);
    
        int a = this.indexOfTab("a");
    
        this.setTabComponentAt(a,aa);
  this.addTab("b",new JTextArea("bbb"));
  
  ButtonTabComponent bb = new ButtonTabComponent(this,connection);
    int b = this.indexOfTab("b");
    this.setTabComponentAt(b,bb);
  
   o = this.getComponents();
   
   
 
   
 
 
for(int i = 1;i< this.getTabCount();i++){  
   
   JTextArea tmp = (JTextArea) this.getComponent(i+1);
   System.out.print(tmp.getText());
}
 
 this.remove(b);
 
 
 for(int i = 1;i<this.getTabCount();i++){  
   
   JTextArea tmp = (JTextArea) this.getComponent(i+1);
   System.out.print(tmp.getText());
}
 
 o = this.getComponents();
 
 this.addTab("c",new JTextArea("ccc"));
 this.addTab("b",new JTextArea("bbb"));
 
 int c = this.indexOfTab("c");
 b = this.indexOfTab("b");
 
 for(int i = 1;i<this.getTabCount();i++){  
   
   JTextArea tmp = (JTextArea) this.getComponent(i+1);
   System.out.print(tmp.getText());
}
 
 o = this.getComponents();
 
 
 
 this.remove(b);
 
    * */
 System.out.print("done");
        
        }catch(Exception e){
        	System.err.println("System error: " + e.getMessage());
        }
    }
    /**
     * 
     * 
     */
    public void createNewTab(String Channel, int tabType){
        SingleTab st;
        
        
        /*
         * adding tab
         */
        
        if(tabType == SingleTab.CHANNEL)
        	st = new SingleTab(connection,Channel,this,SingleTab.CHANNEL);
        else
        	st = new SingleTab(connection,Channel,this,SingleTab.PRIVATE);
        
        Object o = this.getComponents();
        
        this.addTab(Channel,st);
        int tabIndex = this.indexOfTab(Channel); 
        /*
         * adding closebutton
         */
        ButtonTabComponent ctb = new ButtonTabComponent(this,connection);
        
        /*
         * TODO: SET ACTIONLISTENER TO DISCONNECT
         */ 
            //checking whats in the jtabbedpane
  
        this.setTabComponentAt(tabIndex,ctb);
            //checking whats in the jtabbedpane
        o = this.getComponents();
        /*
         * setting focus on new tab
         * connecting to channel
         */
        this.setSelectedIndex(tabIndex);
        try {	
        	if(tabType == SingleTab.CHANNEL)
        		connection.joinChannel(Channel);
        }catch(Exception e){
        	System.err.println("System error" + e.getMessage());
        }
        
        o = this.getComponents();
        
        
        
}
    public void updateTabScreen(String ch, String message) throws BadLocationException{
        
       SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
       
            st.updateScreen(message);
       
    }
   
    /*
     * function overloaded
     */
    void updateTabScreen(String ch, List<String> message) throws BadLocationException {
        
        int tabIndex = this.indexOfTab(ch);
        System.out.printf("updatetab: "+tabIndex);
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.updateScreen(message);

    }
    
    public void updateStatusScreen(String ch){
        try{
        	statusTab.updateScreen(ch);
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
    public boolean tabExists(String tabName){
    	return((this.indexOfTab(tabName) >= 0) ? true : false); //FIX: Sjekk om koden kan forberedres
    	
    }
    
    public void fetchUsers(String ch, Channel c)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.updateUserList(c);
    }

    void userJoined(String nick, String ch) {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.newUserJoined(nick);
    }

    
    void userLeft(String nick, String ch)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.userLeft(nick);
    }
    
    void op(String nick, boolean mode, String ch)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.op(nick, mode);
    }
    
    void voice(String nick, boolean mode, String ch)   {
       SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.voice(nick, mode);
    }
    
    void changedNick(String oldNick, String newNick, String ch)   {
       SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch)+1);
        st.changeNick(oldNick, newNick);
    }
}
