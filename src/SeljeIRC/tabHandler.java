
package SeljeIRC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class tabHandler extends JTabbedPane {
	
    /*
     * indexing all tabs 
     */
    private static ConnectionHandler connection;
    StatusTab statusTab;
    
    
    
    private int curTabIndex = 0;
   
    
     
    public tabHandler(){
        super();
        /* 
         * what im doing?
         */   
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
        this.addTab(I18N.get("channeltab.status"), null, statusTab,"Does nothing");
        }catch(BadLocationException e){
        	System.err.println("System error: " + e.getMessage());
        }
    }
    /**
     * 
     * 
     */
    public void createNewTab(String tabName, int tabType){
        SingleTab st;
        
        
        /*
         * adding tab
         */
        if(!tabName.isEmpty()){
        	
    		
    		if(!tabName.startsWith("#")){		// Appends the hash if not provided
    			StringBuilder stBuild = new StringBuilder();
    			stBuild.insert(0, "#");
    			stBuild.append(tabName);
    			tabName = stBuild.toString();
    		}
        	
    		if(this.indexOfTab(tabName) < 0){			// Checks if tab exists, goes to tab if true
    		
    			if(tabType == SingleTab.CHANNEL)			// Checks the tab type
    				st = new SingleTab(connection,tabName,this,SingleTab.CHANNEL);
    			else
    				st = new SingleTab(connection,tabName,this,SingleTab.PRIVATE);
        
    			this.addTab(tabName, null,st,"Does nothing" );
        
        
    			//System.out.printf("newtab: "+Channel);
        
    			this.addTab(tabName,st);
    			int tabIndex = this.indexOfTab(tabName);

        	

        //debugging
        
        
        
        
        
        System.out.printf("newtab: "+tabIndex);
        
        /*
         * adding closebutton
         */
        //CloseTabButton ctb = new CloseTabButton(this,tabIndex);
        
        
        
        /*
         * setting focus on new tab
         * connecting to channel
         */
        	this.setSelectedIndex(tabIndex);
        	
        	try {	
        		if(tabType == SingleTab.CHANNEL)
        			connection.joinChannel(tabName);
        	}catch(BadLocationException e){
        		System.err.println("System error" + e.getMessage());
        	}
    	}else{		// Already connected to channel
    			this.setSelectedIndex(this.indexOfTab(tabName));
    		}
        
        }else{
        	this.updateStatusScreen("Blank value provided, no tabs created");
        }
}
    public void updateTabScreen(String ch, String message) throws BadLocationException{
        
        
        int tabIndex = this.indexOfTab(ch);
        
        System.out.printf("updateing: "+tabIndex);
       
        Object[] o = this.getComponents();
            SingleTab st = (SingleTab) this.getComponent(tabIndex);
        
            st.updateScreen(message);
       
       
        
        
    }
    /*
     * function overloaded
     */
    void updateTabScreen(String ch, List<String> message) throws BadLocationException {
        
        int tabIndex = this.indexOfTab(ch);
        System.out.printf("updatetab: "+tabIndex);
        SingleTab st = (SingleTab) this.getComponent(tabIndex);
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
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch));
        st.updateUserList(c);
    }

    void userJoined(String nick, String channelName) {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
        st.newUserJoined(nick);
    }

    
    void userLeft(String nick, String channelName)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
        st.userLeft(nick);
    }
    
    void op(String nick, boolean mode, String channelName)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
        st.op(nick, mode);
    }
    
    void voice(String nick, boolean mode, String channelName)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
        st.voice(nick, mode);
    }
    
    /*public void changeTopic(String newTopic, String channelName){
    	SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
    	st.setC(newTopic);
    }*/

    void changedNick(String oldNick, String newNick, String channelName)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(channelName));
        st.changeNick(oldNick, newNick);
    }
    

    
    
    
    class CloseTabButton extends JPanel implements ActionListener {
          private JTabbedPane pane;
          private int indexTab;
          
          public CloseTabButton(JTabbedPane pane, int index) {
            this.pane = pane;
            this.indexTab = index;
            
            
            
            Object[] o = this.pane.getComponents();
            
            
            setOpaque(false);
            add(new JLabel(pane.getTitleAt(index),pane.getIconAt(index),JLabel.LEFT));
            
            //Icon closeIcon = new CloseIcon();
            JButton btClose = new JButton("x");
            btClose.setPreferredSize(new Dimension(10,10));
                add(btClose);
            btClose.addActionListener(this);
            
            System.out.printf("newtab: "+this.indexTab);
            
            o = this.pane.getComponents();
            
            this.pane.setTabComponentAt(this.indexTab, this);
            
            o = this.pane.getComponents();
            
          }
          public void actionPerformed(ActionEvent e) {
            
            
            if (indexTab != -1) {
                connection.disconnectFromChannel(this.pane.getTitleAt(indexTab));
                this.pane.remove(indexTab);
              
            }
          }
    }
}
