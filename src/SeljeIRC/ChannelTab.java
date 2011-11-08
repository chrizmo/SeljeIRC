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
 *  This object is containing all tabs, they are modelled as panels added
 *  directly into the JTabbedPane
 * @author hallvardwestman
 */
//TODO 
public class ChannelTab extends JTabbedPane {
    
    /*
     * indexing all tabs 
     */
    private int tabs = 0;
    
    public ChannelTab(){
        
        /*
         * Could move this functionality to constructor
         */
        createStatusTab();
        
    }
    /**
     * only the statustab is created here, containing just jtextarea
     */
    public void createStatusTab(){
        
        /*
         * Borderlayout containing textarea
         */
        BorderLayout bl = new BorderLayout();
        
        /*
         * addTab takes panel, so this panel is modeled as the "tab-object"
         */
        JPanel panel = new JPanel();
            panel.setLayout(bl);
            panel.setBackground(Color.darkGray);
        
        /*
         * Textarea containing status
         */
         //TODO set up public object of this that can be reached
        JTextArea t = new JTextArea("Status textfieldTest");
            t.setEditable(false);
            
            t.setBackground(Color.lightGray);

       
       /*
        * Layout functionality related to statustab
        */
       JScrollPane textAreaScroller = new JScrollPane(t);
       panel.add(textAreaScroller,BorderLayout.CENTER );
       
      
       this.addTab(I18N.get("channeltab.status"), null, panel,"Does nothing");
    }
    
    /**
     * add tabs for each channel
     */
    public void createNewTab(){
        
        /*
         * Borderlayout containing textarea and userlist
         * 
         */
        BorderLayout bl = new BorderLayout();
        
        /*
         * addTab takes panel, so this panel is modeled as the "tab-object"
         * 
         */
        JPanel panel = new JPanel();
            panel.setLayout(bl);
            panel.setBackground(Color.darkGray);
            
        /*
         * Textarea
         */
        JTextArea t = new JTextArea("textfieldTest");
            t.setEditable(false);
            
            t.setBackground(Color.lightGray);
            
        JScrollPane textAreaScroller = new JScrollPane(t);
        
        
        /*
         * userlist
         */
        //TODO set up the userlist model
        ListOfUsers listPanel = new ListOfUsers();
            listPanel.setBackground(Color.GRAY);
        
            
        /*
         * layout functionality for tabs
         */
        JScrollPane listScroller = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(textAreaScroller,BorderLayout.CENTER );
        panel.add(listScroller,BorderLayout.EAST);
        
        
        tabs++;
        
        this.addTab(I18N.get("channeltab.tab")+tabs, null, panel,"Does nothing");

        
        
        
        
        
    }
}
