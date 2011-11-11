
package SeljeIRC;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author wbserver
 */
public class InputField extends JPanel {
    private JLabel label;
    private JTextField inputField;
    private String channel;
    ConnectionHandler connection;
        
    public InputField(ConnectionHandler con,String cha){
        super();
        channel = cha;
        connection = con;
        //FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        
        
        
        label = new JLabel(I18N.get("inputfield.thechannelname"));
            
        add(label,BorderLayout.WEST);
        
        inputField = new JTextField();
        
        
        add(inputField,BorderLayout.CENTER);
        JButton button = new JButton(I18N.get("inputfield.send"));
        add(button,BorderLayout.EAST);
        
        
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                
                /*
                 * sending input to approporiate screen
                 */
                if(channel == null)
                    connection.sayToServer(inputField.getText());       
                else
                    connection.sayToChannel(inputField.getText(),channel);
                
            }
        });
        

        
    }
    
    
}
