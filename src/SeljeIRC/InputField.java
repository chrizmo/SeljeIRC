
package SeljeIRC;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

/**
 *
 * @author wbserver
 */
public class InputField extends JPanel {
    private JLabel label = new JLabel();
    private JTextField inputField;
    private String channel;
    private int tabType;
    
    private static ConnectionHandler connection = SeljeIRC.connectionHandlerObj.getInstance();
    
    private Pattern inputCommandFinderPattern = Pattern.compile("^/\\w+");

    public InputField(String cha, int TabType){
        super();
        channel = cha;
        //connection = con;				// Removed
        this.tabType = TabType;					// Sets the tabtype to int provided from constructor
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
            	postTextToIRC(inputField);
                
                
            }
        });
        
        inputField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){
                
                /*
                 * sending input to approporiate screen
                 */
            	postTextToIRC(inputField);
            	
                
                
            }
        });


    }
    /**
     * Types the text to the server, and resets the text in the textboks
     * @param txtInputField InputField with text to send
     */
    
    private void postTextToIRC(JTextField txtInputField){
    	int typeOfMessage = tabType;
    	String textToPost = txtInputField.getText();
    	
    	Matcher inputCommandFinder = inputCommandFinderPattern.matcher(textToPost);
    	
        if(inputCommandFinder.find())
        	typeOfMessage = SingleTab.STATUS;
    														// TODO: Make it purty
        if(this.connection == null)							// Checks if the object got initiated correctly
    		this.connection = SeljeIRC.connectionHandlerObj.getInstance();			// IT didn't
        
        try{
        	switch(typeOfMessage){
    			case SingleTab.PRIVATE: connection.sayToPrivate(textToPost, channel); break;
    			case SingleTab.CHANNEL: connection.sayToChannel(textToPost, channel); break; 
    			default: 
                            
                            System.out.print(I18N.get("inputfield.default"));
    				if(tabType == SingleTab.CHANNEL)
    					connection.sayToServer(textToPost,channel);
    				else
    					connection.sayToServer(textToPost,null);
    				break;

        	}
        }catch(BadLocationException e){
        	System.err.println(I18N.get("connection.systemerror") + e.getMessage());
        }catch(NullPointerException ex){
        	System.err.println(I18N.get("inputfield.majorfuckup") + ex.getMessage());
        }
        
    	txtInputField.setText("");
    
    	
    }
    public void setFocusOnField(){
        inputField.requestFocus();
    }
}
