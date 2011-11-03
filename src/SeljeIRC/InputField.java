/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
        
        
    }
    
}
