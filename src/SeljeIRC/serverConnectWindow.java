
package SeljeIRC;

import java.awt.*;
import javax.swing.*;

//import com.sun.tools.hat.internal.model.Root;  The fuck is this?

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jerklib.Session;



/**
 * 
 * @author SljeIRC
 * @since 0.1
 */
//
public class serverConnectWindow extends JFrame{
    
    static int openFrameCount = 0;
    ConnectionHandler connection = SeljeIRC.connectionHandlerObj;
    private Preferences connectionPreferences;
    private int startOfServerLine = 0;							// Used at beginning if server line in ini file
    private static String SERVERFILE = new String("mIRC.ini"); 	// Constant with ini file
    Vector<String> networkNames = new Vector<String>();			// List of networks in list
    Vector<String> serverNames = new Vector<String>();			// List of networks in list
    private Session ircSession; 
    
    /**
     * Creates the connection window for the user
     * @since 0.1
     * @param con ConnectionHandler object provided by main menu
     * 
     */
    public serverConnectWindow(){
        super(I18N.get("serverconnectwindow.connect"));		// Set header title
      
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);	// How to close window
        
        connection = SeljeIRC.connectionHandlerObj;
        // Preferences
        try{
        	connectionPreferences = Preferences.userNodeForPackage(getClass());
        }catch(NullPointerException e){
        	System.err.println("Error in preferences: " + e.getCause());
        }
        
        
        //Layouts
        GridBagLayout totalLayout = new GridBagLayout();
            setLayout(totalLayout);
        GridBagLayout toplayput = new GridBagLayout();
        
        //Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        
        //top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(toplayput);
            
        JPanel invisiblePanel = new JPanel(new FlowLayout());
        
        //Drop Down menus
        
        // The network menus
        networkNames = (Vector<String>)readNetworks(); // Read all the networks from file
        final JComboBox topDropDown = new JComboBox(networkNames);
        
        // The server menus
        topDropDown.setSelectedItem(new String(connectionPreferences.get("lastnetwork", "")));
        serverNames = (Vector<String>)readServers(topDropDown.getSelectedItem().toString()); // Getting servers for element one
        final JComboBox subDropDown = new JComboBox(new DefaultComboBoxModel(serverNames));
        subDropDown.setSelectedItem(connectionPreferences.get("lastserver",""));
        //TODO: Choose last server (Christer)
        

        topDropDown.addActionListener (new ActionListener(){
        	public void actionPerformed(ActionEvent evt){
        		serverNames.clear();
        		serverNames = (Vector<String>)readServers(topDropDown.getSelectedItem().toString());
        		subDropDown.setModel(new DefaultComboBoxModel(serverNames));
        	}
        });
            

        //Labels
        JLabel topLabel = new JLabel(I18N.get("serverconnectwindow.ircnetwork"));
        
        gbc.insets = new Insets(1,1,1,1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        toplayput.setConstraints(topLabel, gbc);
        
        //adding toplayout to gridbag
        topPanel.add(topLabel);
        
        // --- Set constraints --- // 
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc	.gridy = 0;
        gbc.weightx = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        toplayput.setConstraints(topDropDown, gbc);
        
        topPanel.add(topDropDown);
        
        
        //top panel constraints
        gbc.fill=GridBagConstraints.HORIZONTAL;
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
            
            JButton addSomething = new JButton(I18N.get("serverconnectwindow.add"));
            addSomething.setName("ADD");
            gbc.fill=GridBagConstraints.HORIZONTAL;
            gbc.gridx=0;
            gbc.gridy=0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.anchor = GridBagConstraints.WEST;
            rightLayout.setConstraints(addSomething,gbc);
            rightPanel.add(addSomething);
            
            addSomething.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent evt){
            		String serverName = new String();
            		
            		serverName = JOptionPane.showInputDialog(serverConnectWindow.this,"dettefunker heller ikke for hallvard");
            		serverNames.add(serverName);
            		subDropDown.setModel(new DefaultComboBoxModel(serverNames));
            		subDropDown.setSelectedItem(serverName);
            		
            		writeServer(serverName,topDropDown.getSelectedItem().toString());
            	}
            	
            	
            });
            
            //changbutton
            JButton changeSomething = new JButton(I18N.get("serverconnectwindow.change"));
            gbc.gridy=1;
            changeSomething.setName("CHANGE");
            rightLayout.setConstraints(changeSomething,gbc);
            rightPanel.add(changeSomething);
            
            changeSomething.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent evt){
            		String newServerName;
            		try{
            			if(serverNames.size() > 0){		// Check if there are servers defined
            				int serverVectorPosition = serverNames.indexOf(subDropDown.getSelectedItem());
            				String serverName = serverNames.get(serverVectorPosition);
            				newServerName = JOptionPane.showInputDialog(serverConnectWindow.this,I18N.get("serverconnectwindow.addnewserver"),serverName);
            				changeServer(serverName, newServerName);
            				serverNames.setElementAt(newServerName, serverVectorPosition);
            				subDropDown.setModel(new DefaultComboBoxModel(serverNames));
            				subDropDown.setSelectedItem(newServerName);
            			}
            		}catch(Exception e){
            			System.err.println("Error while adding: " + e.getMessage());
            		}
            		
            	}
            });
            
            JButton deleteSomething = new JButton(I18N.get("serverconnectwindow.delete"));
            deleteSomething.setName("REMOVE");
            gbc.gridy=2;
            rightLayout.setConstraints(deleteSomething,gbc);
            rightPanel.add(deleteSomething);
            
            
            deleteSomething.addActionListener(new ActionListener(){ // Delete server from network
            	public void actionPerformed(ActionEvent evt){
            		try{
            			if(serverNames.size() > 0){		// Check if there are servers defined
            				int serverVectorPosition = serverNames.indexOf(subDropDown.getSelectedItem());
            				deleteServer(serverNames.get(serverVectorPosition));		// Delete from file
            				serverNames.remove(serverVectorPosition);
            				subDropDown.setModel(new DefaultComboBoxModel(serverNames));
            				if(serverVectorPosition > 0)	// Checks if the vector is zero 
            					subDropDown.setSelectedIndex(serverVectorPosition - 1);
            			}
            		}catch(Exception e){
            			System.err.println("Error while deleting server: " + e.getMessage());
            		}
            	}
            	
            });
            
            JButton sortSomething = new JButton(I18N.get("serverconnectwindow.sort"));
            sortSomething.setName("SORT");
            gbc.gridy=3;
            rightLayout.setConstraints(sortSomething,gbc);
            rightPanel.add(sortSomething);
            
            sortSomething.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent evt){
            		try{
            			if(serverNames.size() > 0){
            				String selectedServer = subDropDown.getSelectedItem().toString();
            				serverNames = sortServers(serverNames);
            				subDropDown.setModel(new DefaultComboBoxModel(serverNames));
            				subDropDown.setSelectedItem(selectedServer);
            			}
            		}catch(Exception e){
            			System.err.println("Error while sorting" + e.getMessage());
            		}
            	}

				private Vector<String> sortServers(Vector<String> serverNames) {
					Vector<String> tVector = serverNames;
					try{	 
						Collections.sort(tVector);
					
					}catch(Exception e){
						System.err.println("Error while sorting" + e.getMessage());
					}
					
					return tVector;
				}
            });
            
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
       
        

        final JTextField nameField = new JTextField(connectionPreferences.get("name", ""));
        final JTextField emailField = new JTextField(connectionPreferences.get("email",""));
        final JTextField nicNameField = new JTextField(connectionPreferences.get("nickname",""),10);
        final JTextField aliasField = new JTextField(connectionPreferences.get("alias",""),10);

        /* Textfields
         *
         */
        
        //Connect button
        JButton connect = new JButton(I18N.get(("serverconnectwindow.connect")));
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets=new Insets(2,2,2,2);
        gbc.gridy=2;
        gbc.gridheight = 2;
        totalLayout.setConstraints(connect,gbc);
        add(connect);
        
        
        
        connect.addActionListener(new ActionListener() {//TODO: Legg til connectionsjekk (Christer)
         public void actionPerformed( ActionEvent e)// TODO: Legg til bekreftelse om reconnect -"
            { // Get server and nick, and run the connection // HUSK � SLETTE ALLE TABS
             String s = subDropDown.getSelectedItem().toString();
             String n = nicNameField.getText();
          
             	if(connection.connectedToServer()){		// Check if client is connected
             		if((JOptionPane.showConfirmDialog(serverConnectWindow.this, "reconnect" ,"connect",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)) == JOptionPane.YES_OPTION){
             			connection.closeConnection();
             			connection.connectIt(s, n);
                        connectionPreferences.put("lastnetwork", topDropDown.getSelectedItem().toString());
                        connectionPreferences.put("lastserver", s);
             		}             			
             	}else{
             		connection.connectIt(s, n);
                    connectionPreferences.put("lastnetwork", topDropDown.getSelectedItem().toString());
                    connectionPreferences.put("lastserver", s);
            		connectionPreferences.put("nickname",nicNameField.getText());
            		connectionPreferences.put("email",nicNameField.getText());
            		connectionPreferences.put("alias",aliasField.getText());
            		connectionPreferences.put("name",nameField.getText());
                   
             	}
                
                // Save all the configuration
                

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
        
        gbc.insets=new Insets(1,10,1,1);
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
        gbc.insets=new Insets(1,1,1,1);
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
        JLabel invisibleLabel = new JLabel(I18N.get("serverconnectwindow.invisible"));
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
                        
            okButton.addActionListener(new ActionListener(){ //TODO: Legg til brukernavnsjekk (CHRISTER)
            	public void actionPerformed(ActionEvent actEvt){ // Saves configuration to file
            		
            		if(!connection.connectedToServer()){
            			connectionPreferences.put("nickname",nicNameField.getText());
            			connectionPreferences.put("email",nicNameField.getText());
            			connectionPreferences.put("alias",aliasField.getText());
            			connectionPreferences.put("name",nameField.getText());
            		}
            		serverConnectWindow.this.setVisible(false);
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
    /**
     * Connects to servre
     * 
     * @param channel String of channel to connect to
     * @deprecated 
     */
    
    public void joinChannel(String channel) {


       //connectToServer.joinChannel(null, channel);
    }
 	
    	 /**
    	  * Reads networks from a .ini files
    	  * @return Vector<String> with list of networks
    	  */
   	public Vector<String> readNetworks(){
		BufferedReader brReader;
		Vector<String> networkList = new Vector<String>();
		String readFileLine = null;
		Pattern networkExpression = Pattern.compile("(?!^n[0-9]+=)[A-z]*$");
		Matcher networkMatcher = null;
		try{
			brReader = this.openIniFile(this.SERVERFILE);
			
			while((readFileLine = brReader.readLine()) != null && !readFileLine.equals("[networks]")){} // Read down to line 
			
			while(!(readFileLine = brReader.readLine()).isEmpty()){
					networkMatcher = networkExpression.matcher(readFileLine);

					if(networkMatcher.find()){
						networkList.add(networkMatcher.group().toString());
					}
				}	
			brReader.close();
			return networkList;
		}catch(IOException ioe){
			System.err.println("Error while reading network: " + ioe.getMessage());
			return null;
		}
		
	}	
   	/**
   	 * Reads servers based on the provided network from ini file
   	 * @param networkName String with the network to find servers
   	 * @return Vector<String> list of servers
   	 */
   	public Vector<String> readServers(String networkName){
   		int returnValue = 0;
		BufferedReader brReader;
		Vector<String> serverList = new Vector<String>();
		String readFileLine = null;
		try{
			brReader = this.openIniFile(this.SERVERFILE);
			
			while((readFileLine = brReader.readLine()) != null && !readFileLine.equals("[servers]")){} // Read down to line 
	
			while((readFileLine = brReader.readLine()) != null){
				returnValue = Integer.parseInt(readFileLine.substring(1, readFileLine.indexOf("=")));		// Reads the first integer thing at start of string
					if(readFileLine.endsWith(networkName)){
						serverList.add(readFileLine.split(":")[1]);
					}
				}
			
			this.startOfServerLine = returnValue;					// The number at the beginning of the string

			brReader.close();
			return serverList;
		}catch(IOException ioe){
			System.err.println("Error while reading network: " + ioe.getMessage());
			return null;
		}
		
	}
   	/**
   	 * Adds changes to a server from the list to the server file.
   	 * 
   	 * This works by reading the old file and putting the contents in a new file. If a line contains
   	 * the old server name that substring gets replaced with the new string 
   	 * 
   	 * @author Christer Vaskinn
   	 * @since 0.1
   	 * @param oldServerName	String with the old servername from the list
   	 * @param newServerName String with the new servername to be put in the list
   	 */
   	private void changeServer(String oldServerName, String newServerName){
   		File tempFile = new File("TemporaryFile.ini");		// The fileobject for the NEW file
   		File serverFile = new File(this.SERVERFILE);		// The fileobject of the OLD file
   		
   		BufferedReader brReader;							// The object to be read from the file 
   		BufferedWriter tmpFileWriter;						// The object to write to the new file
   		String lineFromFile;								// The file read from the old file
   		try{
   			brReader = new BufferedReader(new FileReader(serverFile));
   			tmpFileWriter = new BufferedWriter(new FileWriter(tempFile));
   			
   			
   			if(serverFile.exists()){							// Checks if the old file exists
   				while((lineFromFile = brReader.readLine()) != null){
   					if(lineFromFile.contains(oldServerName)){
   						lineFromFile = lineFromFile.replaceFirst(oldServerName, newServerName);
   					}
   						tmpFileWriter.write(lineFromFile);
   						tmpFileWriter.newLine();
   				}
   				brReader.close();							// Closes both the files
   				tmpFileWriter.close();
   			
   				serverFile.delete();						// Deletes the old file
   				tempFile.renameTo(serverFile);				// Rename the old file to the new file
   			}
   		}catch(Exception ioe){								// Shit got real
   			System.err.println("Error while deleting server from file" + ioe.getMessage());
   		}
   	}
   	
   	/**
   	 * Deletes a server from the server list file
   	 * 
   	 * Works by writing contents of the serverfile line for line to a temporary file. 
   	 * If a line contains the servername it get's ignored and the file continous writing the rest of the old files contents.
   	 * The old file then gets deleted and the new file gets renamed to the old files name. 
   	 *  
   	 * @author Christer Vaskinn
   	 * @since 0.1
   	 * @param serverName String with the servername to delete
   	 */
   	
   	private void deleteServer(String serverName){
   		File tempFile = new File("TemporaryFile.ini");				// Fileobject for the new temporary file
   		File serverFile = new File(this.SERVERFILE);				// Fileobject of the old serverfile to be read from
   		
   		BufferedReader brReader;									// BufferedReader to write line for line
   		BufferedWriter tmpFileWriter;								// BufferedWriter to write to file
   		String lineFromFile;										// Line read from the old file
   		try{
   			brReader = new BufferedReader(new FileReader(serverFile));
   			tmpFileWriter = new BufferedWriter(new FileWriter(tempFile));
   			
   			while((lineFromFile = brReader.readLine()) != null){	// While the file has contents
   				if(!lineFromFile.contains(serverName)){				// Checks if the read line contains servername
   					tmpFileWriter.write(lineFromFile);				// If _NOT_ the line gets written to a new file
   					tmpFileWriter.newLine();						// Create new line
   				}
   				
   			}
   			brReader.close();										// Closes both files
   			tmpFileWriter.close();
   				
   			serverFile.delete();									// Delete the old file
   			tempFile.renameTo(serverFile);							// Rename new file to old filename
   			
   		}catch(Exception ioe){										// Why would you care?
   			System.err.println("Error while deleting server from file" + ioe.getMessage());
   		}
   	}
   	/**
   	 * Writes the server name added to the list
   	 * 
   	 * Just adds a line at the end of the file with an incremented n-number and network name
   	 * 
   	 * @author Christer Vaskinn
   	 * @since 0.1
   	 * @param serverName String with the servername to add to the list
   	 * @param networkName String with the networkName to add to the end of the string
   	 */
   	private void writeServer(String serverName, String networkName){
   		
   		BufferedWriter brWriter;							// Buffered writerobject 
   		String stringToWrite;								// The string to append to file
   		
   		try{
   			stringToWrite = "n" + ++this.startOfServerLine + "=Random serverSERVER:" + serverName.toLowerCase() + ":6660-6667GROUP:" + networkName; // Construct the string to write, increment n-number
   			brWriter = new BufferedWriter(new FileWriter(this.SERVERFILE, true)); 		// Create the writing object		
   			brWriter.write(stringToWrite);												// Write the file to the file
   			brWriter.newLine();															// A new line for pretty prettyness about pretty ponies. I'm fucking bored
   			brWriter.close();															// Close the file 
   		}catch(IOException ioe){														// This is not the hip place to be daddy'o
   			System.err.println("Error while writing to file: " + ioe.getMessage());
   		}
   		
   	}
    	
		/**
    	 * Opens a file for reading purposes.
    	 * 
    	 * @param filename FileName of file to open
    	 * @return A Buffered reader object to the file, null if file doesn't exists
    	 * 
    	 *
    	 */ 
    	private BufferedReader openIniFile(String filename){
    		BufferedReader brReader = null;					// The Reader object to return
    		try{
    			File fileObj = new File(filename);			// The fileobject of the file to read
    			if(fileObj.exists())						// If the file exits, create bufferedReader
    				brReader = new BufferedReader(new FileReader(fileObj));
    			return brReader;							// Wee
    		}catch(FileNotFoundException fileException){	// Well why did this happen?
    			System.err.println("Error opening file: " + fileException.getMessage());
    			return null;
    		}
    	
    	}

}