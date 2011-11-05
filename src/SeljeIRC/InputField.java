/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author wbserver
 */
public class InputField extends JPanel {
    private JLabel label;
    private JTextField channelName;
        
    public InputField(){
        super();
        
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        
        setLayout(layout);
        
        label = new JLabel("thechannelname"); 
        add(label);
        
        channelName = new JTextField();
        
        
        add(channelName);
        
        /* Jon Arne be testing
        Border blackline = BorderFactory.createLineBorder(Color.black, 1);
        
        JTextArea u = new JTextArea("Her kan vi skrive masse tekst   ");
        u.setEditable(true);
        u.setBorder(blackline);
        add(u);
        
        JButton button = new JButton("Send");
        add(button);
        
        */
        
    }
    
}
