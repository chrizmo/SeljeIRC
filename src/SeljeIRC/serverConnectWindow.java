/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.*;



/**
 *
 * @author hallvardwestman
 */
//
public class serverConnectWindow extends JFrame{
    
    static int openFrameCount = 0;
    ConnectToServer connectToServer;

    public serverConnectWindow(){
        super(I18N.get("ServerConnectWindow.Title"));
        //Strings
        String networkName[] = {"Efnet","DALnet","Undernet","Gakk Gakk"};
        String subNetworkName[] = {"irc.homelien.no","efnet.xs4all.nl","localhost","irc.du.se"};
        
        //Layouts
        GridBagLayout totalLayout = new GridBagLayout();
            setLayout(totalLayout);
        FlowLayout toplayput = new FlowLayout();
        
        //Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        //top panel
        JPanel topPanel = new JPanel();
            topPanel.setLayout(toplayput);
            
        JPanel invisiblePanel = new JPanel(new FlowLayout());
        
        
        //Drop downs
        final JComboBox topDropDown = new JComboBox(networkName);
        final JComboBox subDropDown = new JComboBox(subNetworkName);
        
            
        
        //Labels
        JLabel topLabel = new JLabel(I18N.get("ServerConnectWindow.IRCNetworkLabel"));
        
        
        //adding toplayout to gridbag
        topPanel.add(topLabel);
        topPanel.add(topDropDown);
        
        
        //top panel constraints
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        totalLayout.setConstraints(topPanel, gbc);
        add(topPanel);
        
            //top right menu
        
        JPanel rightPanel = new JPanel();
        GridBagLayout rightLayout = new GridBagLayout();
            rightPanel.setLayout(rightLayout);
            //addbutton
            JButton addSomething = new JButton(I18N.get("ServerConnectWindow.Add"));
            gbc.fill=GridBagConstraints.NONE;
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.anchor = GridBagConstraints.WEST;
            rightLayout.setConstraints(addSomething,gbc);
            rightPanel.add(addSomething);
            
            //changbutton
            JButton changeSomething = new JButton(I18N.get("ServerConnectWindow.Edit"));
            gbc.gridy=1;
            rightLayout.setConstraints(changeSomething,gbc);
            rightPanel.add(changeSomething);
            
            JButton deleteSomething = new JButton(I18N.get("ServerConnectWindow.Delete"));
            gbc.gridy=2;
            rightLayout.setConstraints(deleteSomething,gbc);
            rightPanel.add(deleteSomething);
            
            JButton sortSomething = new JButton(I18N.get("ServerConnectWindow.Sort"));
            gbc.gridy=3;
            rightLayout.setConstraints(sortSomething,gbc);
            rightPanel.add(sortSomething);
            
            
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=3;
        gbc.gridy=0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.WEST;
        totalLayout.setConstraints(rightPanel,gbc);
        add(rightPanel);    
            
            
            
        //subnet drop down   
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridheight = 1;
        totalLayout.setConstraints(subDropDown,gbc);
        add(subDropDown);    
       
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        final JTextField nicNameField = new JTextField(10);
        JTextField aliasField = new JTextField(10);

        /* Textfields
         *
         */
        
        //Connect button
        JButton connect = new JButton(I18N.get("ServerConnectWindow.ConnectButton"));
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets=new Insets(2,2,2,2);
        gbc.gridy=2;
        gbc.gridheight = 2;
        totalLayout.setConstraints(connect,gbc);
        add(connect);
        connect.addActionListener(new ActionListener() {
         public void actionPerformed( ActionEvent e)
            { // Get server and nick, and run the connection
             String s = subDropDown.getSelectedItem().toString();
             String n = nicNameField.getText();
             
             System.out.println( s + " - " + n);
             	
                connectToServer = new ConnectToServer(
                s, n);
                
                serverConnectWindow.this.setVisible(false);	


            }

        });
        
        //left coloumn
        
        JLabel name = new JLabel(I18N.get("std.Name"));
        JLabel email = new JLabel(I18N.get("std.Email"));
        JLabel nicName = new JLabel(I18N.get("std.Nickname"));
        JLabel alias = new JLabel(I18N.get("std.Alias"));
        
        /* TextLabels
         * 
         */
        
        gbc.insets=new Insets(1,1,1,1);
        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridheight = 1;
        totalLayout.setConstraints(name,gbc);
        add(name);
        
        gbc.gridy=5;
        totalLayout.setConstraints(email,gbc);
        add(email);
        
        gbc.gridy=6;
        totalLayout.setConstraints(nicName,gbc);
        add(nicName);
        
        gbc.gridy=7;
        totalLayout.setConstraints(alias,gbc);
        add(alias);

        
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=1;
        gbc.gridy=4;
        gbc.anchor = GridBagConstraints.WEST;
        totalLayout.setConstraints(nameField,gbc);
        add(nameField);
        
        gbc.gridy=5;
        totalLayout.setConstraints(emailField,gbc);
        add(emailField);
        
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy=6;
        totalLayout.setConstraints(nicNameField,gbc);
        add(nicNameField);
        
        gbc.gridy=7;
        totalLayout.setConstraints(aliasField,gbc);
        add(aliasField);
        
        
        JCheckBox invisibleBox = new JCheckBox();
        JLabel invisibleLabel = new JLabel(I18N.get("ServerConnectWindow.InvisibleModeLabel"));
        invisiblePanel.add(invisibleBox);
        invisiblePanel.add(invisibleLabel);
        
        gbc.gridy=8;
        totalLayout.setConstraints(invisiblePanel,gbc);
        add(invisiblePanel); 
        
        
        /* bottom buttons
         * 
         */
        JPanel bottomButtons = new JPanel();
        GridBagLayout bottomLayout = new GridBagLayout();
        
        bottomButtons.setLayout(bottomLayout);
        
            
            JButton okButton = new JButton(I18N.get("std.OK"));
            JButton abortButton = new JButton(I18N.get("std.Abort"));
            JButton helpButton = new JButton(I18N.get("std.Help"));
        
            /* --- Bottom Actionlisteners --- */
                        
            okButton.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent actEvt){
            		JOptionPane.showMessageDialog(serverConnectWindow.this, "The buttons, they do NOTHING!");
            	}
            });
            
            helpButton.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent actEvt){
            		JOptionPane.showMessageDialog(serverConnectWindow.this, "The buttons, they do NOTHING!");
            	}
            });
            
            abortButton.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent actEvent){
            		serverConnectWindow.this.setVisible(false);		// Hides box
            	}
            });
            
            
            gbc.fill=GridBagConstraints.NONE;
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.anchor = GridBagConstraints.WEST;
        
            bottomLayout.setConstraints(okButton,gbc);
            bottomButtons.add(okButton);
            
           
            gbc.gridx=1;
            bottomLayout.setConstraints(abortButton,gbc);
            bottomButtons.add(abortButton);
            
            gbc.gridx=2;
            bottomLayout.setConstraints(helpButton,gbc);
            bottomButtons.add(helpButton);
          
        
        
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=1;
        gbc.gridy=9;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        totalLayout.setConstraints(bottomButtons,gbc);
        add(bottomButtons);
        
    }

    public void joinChannel(String channel) {


       connectToServer.joinChannel(null, channel);
    }
}
