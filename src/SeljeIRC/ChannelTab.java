
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
public class ChannelTab extends JTabbedPane {
	
    /*
     * indexing all tabs 
     */
    private static ConnectionHandler connection;
    StatusTab statusTab;
    
    private JTabbedPane pane = this;
    
    
    private int curTabIndex = 0;
   
    
     
    public ChannelTab(){
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
    public void createNewTab(String Channel, int tabType){
        SingleTab st;
        
        if(tabType == SingleTab.CHANNEL)
        	st = new SingleTab(connection,Channel,this,SingleTab.CHANNEL);
        else
        	st = new SingleTab(connection,Channel,this,SingleTab.PRIVATE);
        
        this.addTab(Channel, null,st,"Does nothing" );
        
        int tabIndex = this.indexOfTab(Channel);
        
        //CloseTabButton ctb = new CloseTabButton(pane,tabIndex);
        
        
        this.setSelectedIndex(tabIndex);
        	
        if(tabType == SingleTab.CHANNEL)
            connection.joinChannel(Channel);
    
}
    public void updateTabScreen(String ch, String message){
        
       
        int tabIndex = this.indexOfTab(ch);
        
        
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
        this.removeAll();
    }

    void updateTabScreen(String ch, List<String> message) {
        int tabIndex = this.indexOfTab(ch);
        SingleTab st = (SingleTab) this.getComponent(tabIndex);
        st.updateScreen(message);

    }
    // Checks if a certain tab exists
    public boolean tabExists(String tabName){
    	return((this.indexOfTab(tabName) >= 0) ? true : false); //FIX: Sjekk om koden kan forberedres
    	
    }
    
    public void fetchUsers(String ch, Channel c)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch));
        st.updateUserList(c);
    }
    
    
    
    class CloseTabButton extends JPanel implements ActionListener {
          private JTabbedPane pane;
          private int index;
          public CloseTabButton(JTabbedPane pane, int index) {
            this.pane = pane;
            this.index = index;
            setOpaque(false);
            add(new JLabel(
                pane.getTitleAt(index),
                pane.getIconAt(index),
                JLabel.LEFT));
            //Icon closeIcon = new CloseIcon();
            JButton btClose = new JButton("x");
            btClose.setPreferredSize(new Dimension(10,10));
            add(btClose);
            btClose.addActionListener(this);
            pane.setTabComponentAt(index, this);
          }
          public void actionPerformed(ActionEvent e) {
            
            
            if (index != -1) {
                connection.disconnectFromChannel(pane.getTitleAt(index));
                pane.remove(index);
              
            }
          }
    }
}
