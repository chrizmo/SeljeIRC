
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
    private JLabel label = new JLabel();
    private JTextField inputField;
    private String channel;
    private int tabType;
    connectionHandler connection;
        
    public InputField(connectionHandler con, String cha, int TabType){
        super();
        channel = cha;
        connection = con;
        tabType = TabType;
        //FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        
        
        label = new JLabel(cha);
            
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
                switch(tabType){
                	case SingleTab.PRIVATE: connection.sayToPrivate(inputField.getText(), channel); break;
                	case SingleTab.CHANNEL: connection.sayToChannel(inputField.getText(),channel); break;        
                	default: connection.sayToServer(inputField.getText()); break;
                }
                inputField.setText("");
                
            }
        });
        
        inputField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){
                
                /*
                 * sending input to approporiate screen
                 */
                switch(tabType){
                	case SingleTab.PRIVATE: connection.sayToPrivate(inputField.getText(), channel); break;
                	case SingleTab.CHANNEL: connection.sayToChannel(inputField.getText(),channel); break;        
                	default: connection.sayToServer(inputField.getText()); break;
                }
                inputField.setText("");
                
                
            }
        });


    }
    /**
     * Sets the input Label next to the thingy
     * @param channelName
     */
    
    public void setInputLabel(String channelName){
    	try{
    		this.label.setText("Tst");
    	}catch(Exception e){
    		System.err.println(e.getCause() + " crashed and fuck you: " + e.getMessage());
    	}
    	
    }
}
