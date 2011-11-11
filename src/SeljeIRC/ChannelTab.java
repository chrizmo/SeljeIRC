
package SeljeIRC;

import java.util.Vector;
import javax.swing.JTabbedPane;
import jerklib.Channel;
import jerklib.events.MessageEvent;

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
     */
    
    public void createStatusTab(){
        statusTab = new StatusTab(connection);
        
        this.addTab(I18N.get("channeltab.status"), null, statusTab,"Does nothing");
    }
    /*
     * 
     */
    public void createNewTab(String Channel){
        
        
        
        SingleTab st = new SingleTab(connection,Channel);
        
        this.addTab(Channel, null, st,"Does nothing");
        
        this.setSelectedIndex(this.indexOfTab(Channel));
        
        connection.joinChannel(Channel);
        
        
        
    }
    public void updateTabScreen(String ch, String message){
        
       
        int tabIndex = this.indexOfTab(ch);
        
        
        SingleTab st = (SingleTab) this.getComponent(tabIndex);
        
        st.updateScreen(message);
        
    }
    public void updateStatusScreen(String ch){
        statusTab.updateScreen(ch);
    }
    
    public static void setConnection(ConnectionHandler ch){
        connection = ch;
    }
    
    public void removeAllTabs(){
        this.removeAll();
    }
    
    
    
    
}
