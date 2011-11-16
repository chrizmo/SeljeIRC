
package SeljeIRC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.List;
import javax.swing.JTabbedPane;
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
     */
    
    public void createStatusTab(){
        statusTab = new StatusTab(connection);
        
        this.addTab(I18N.get("channeltab.status"), null, statusTab,"Does nothing");
        
        
        
    }
    /*
     * 
     */
    public void createNewTab(String Channel){
        
        SingleTab st = new SingleTab(connection,Channel,this);
        
        /*
         * adding tab
         */
        
        
        
        System.out.printf("newtab: "+Channel);
        
        this.addTab(Channel,st);
        int tabIndex = this.indexOfTab(Channel); 
            
        //debugging
        
        
        
        
        
        System.out.printf("newtab: "+tabIndex);
        
        /*
         * adding closebutton
         */
        CloseTabButton ctb = new CloseTabButton(this,tabIndex);
        
        
        
        /*
         * setting focus on new tab
         * connecting to channel
         */
        
        
        
        this.setSelectedIndex(tabIndex);
        
        connection.joinChannel(Channel);
        
        
    }
    public void updateTabScreen(String ch, String message){
        
        
        int tabIndex = this.indexOfTab(ch);
        
        System.out.printf("updateing: "+tabIndex);
       
        Object[] o = this.getComponents();
            SingleTab st = (SingleTab) this.getComponent(tabIndex);
        
            st.updateScreen(message);
       
       
        
        
    }
    /*
     * function overloaded
     */
    void updateTabScreen(String ch, List<String> message) {
        
        int tabIndex = this.indexOfTab(ch);
        System.out.printf("updatetab: "+tabIndex);
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
        
        for(int i = 1; i <= this.getTabCount(); i++){
            
            this.remove(i);
            
        }
    }

    
    
    public void fetchUsers(String ch, Channel c)   {
        SingleTab st = (SingleTab) this.getComponent(this.indexOfTab(ch));
        st.updateUserList(c);
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
