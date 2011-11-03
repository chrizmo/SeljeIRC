/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.ScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 *
 * @author wbserver
 */
public class ChannelTab extends JTabbedPane {
    
    public ChannelTab(){
        createNewTab();
        
    }
    public void createNewTab(){
        
        JTextArea t = new JTextArea("textfieldTest");
        t.setEditable(false);
        
        ScrollPane s = new ScrollPane();
        s.add(t);
        
        this.addTab("Tab 1", null, s,"Does nothing");
        
        
        
    }
    
    
}
