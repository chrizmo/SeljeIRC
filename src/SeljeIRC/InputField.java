/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;


import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        
        //FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        
        
        
        label = new JLabel(I18N.get("inputfield.thechannelname"));
            
        add(label,BorderLayout.WEST);
        
        channelName = new JTextField();
        
        
        add(channelName,BorderLayout.CENTER);
        JButton button = new JButton(I18N.get("inputfield.send"));
        add(button,BorderLayout.EAST);
        
        
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
