/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 *
 * @author wbserver
 */
public class ChannelTab extends JTabbedPane {
    
    private int tabs = 0;
    
    public ChannelTab(){
        createStatusTab();
        
    }
    
    public void createStatusTab(){
        BorderLayout bl = new BorderLayout();
        
        JPanel panel = new JPanel();
            panel.setLayout(bl);
            panel.setBackground(Color.darkGray);
            
        JTextArea t = new JTextArea("textfieldTest");
            t.setEditable(false);
            
            t.setBackground(Color.lightGray);
            
        JScrollPane textAreaScroller = new JScrollPane(t);
       panel.add(textAreaScroller,BorderLayout.CENTER );
       
      
       this.addTab("Status", null, panel,"Does nothing");
    }
    public void createNewTab(){
        
        BorderLayout bl = new BorderLayout();
        
        JPanel panel = new JPanel();
            panel.setLayout(bl);
            panel.setBackground(Color.darkGray);
            
            
            
        
        JTextArea t = new JTextArea("textfieldTest");
            t.setEditable(false);
            
            t.setBackground(Color.lightGray);
            
        JScrollPane textAreaScroller = new JScrollPane(t);
        
        
                
        ListOfUsers listPanel = new ListOfUsers();
            listPanel.setBackground(Color.GRAY);
            
        JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(textAreaScroller,BorderLayout.CENTER );
        panel.add(listScroller,BorderLayout.EAST);
        
        
        tabs++;
        
        this.addTab("Tab "+tabs, null, panel,"Does nothing");
        
        
        
        
    }
}
